package com.example.strip.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.strip.Adapters.TripAdapter;
import com.example.strip.Models.Trip;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.network.ApiClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeDriverFragment extends Fragment {
    private RecyclerView recyclerViewTrips;
    private TripAdapter tripAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_driver, container, false);

        recyclerViewTrips = view.findViewById(R.id.recyclerViewTrips);

        // Set up RecyclerView with horizontal scrolling
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTrips.setLayoutManager(layoutManager);
        loadTrips();
        return view;
    }

    private void loadTrips() {
        ITripMobileApiService tripService = ApiClient.getClientWithToken(requireContext()).create(ITripMobileApiService.class);
        tripService.getAllTrips().enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(@NonNull Call<List<Trip>> call, @NonNull Response<List<Trip>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Trip> trips = response.body();

                    // Sort newest first (descending), assuming getCreatedDate() returns a Date or LocalDateTime
                    Collections.sort(trips, (t1, t2) -> t2.getHandleId().compareTo(t1.getHandleId()));

                    tripAdapter = new TripAdapter(getContext(), trips);
                    recyclerViewTrips.setAdapter(tripAdapter);
                } else {
                    Log.e("Failed", "Failed to load trips!" + response.code());
                    Toast.makeText(getContext(), "Failed to load trips!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Trip>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}