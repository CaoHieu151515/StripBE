package com.example.strip.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strip.Adapters.TripAdapter;
import com.example.strip.Models.Trip;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.network.ApiClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeDriverFragment extends Fragment {
    private RecyclerView recyclerViewTrips;
    private TripAdapter tripAdapter;
    private TextView tvLoading;
    private EditText edFindByLocation;
    private ImageView ivSearchLocation;
    private List<Trip> allTrips = new ArrayList<>(); // Giữ toàn bộ danh sách

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_driver, container, false);

        tvLoading = view.findViewById(R.id.tvLoading);
        recyclerViewTrips = view.findViewById(R.id.recyclerViewTrips);
        edFindByLocation = view.findViewById(R.id.edFindByLocation);
        ivSearchLocation = view.findViewById(R.id.ivSearchLocation);

        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ivSearchLocation.setOnClickListener(v -> performSearch());

        loadTrips();
        return view;
    }

    private void loadTrips() {
        tvLoading.setVisibility(View.VISIBLE);
        recyclerViewTrips.setVisibility(View.GONE);

        ITripMobileApiService tripService = ApiClient.getClientWithToken(requireContext()).create(ITripMobileApiService.class);
        tripService.getAllTrips().enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(@NonNull Call<List<Trip>> call, @NonNull Response<List<Trip>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    allTrips = response.body(); // Lưu tất cả dữ liệu
                    Collections.sort(allTrips, (t1, t2) -> t2.getHandleId().compareTo(t1.getHandleId()));
                    updateTripList(allTrips);
                } else {
                    tvLoading.setText("Failed to load trips.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Trip>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                tvLoading.setText("Error: " + t.getMessage());
            }
        });
    }

    private void updateTripList(List<Trip> trips) {
        tripAdapter = new TripAdapter(getContext(), trips);
        recyclerViewTrips.setAdapter(tripAdapter);
        tvLoading.setVisibility(View.GONE);
        recyclerViewTrips.setVisibility(View.VISIBLE);
    }

    private void performSearch() {
        String keyword = edFindByLocation.getText().toString().trim().toLowerCase();
        if (keyword.isEmpty()) {
            updateTripList(allTrips); // Show all if empty
            return;
        }

        List<Trip> filteredTrips = new ArrayList<>();
        for (Trip trip : allTrips) {
            if ((trip.getStartLocation() != null && trip.getStartLocation().toLowerCase().contains(keyword)) ||
                    (trip.getEndLocation() != null && trip.getEndLocation().toLowerCase().contains(keyword))) {
                filteredTrips.add(trip);
            }
        }

        updateTripList(filteredTrips);
    }
}