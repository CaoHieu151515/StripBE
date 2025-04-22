package com.example.strip.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.strip.Activities.Driver.AddTripActivity;
import com.example.strip.Activities.Driver.ManageTripActivity;
import com.example.strip.Adapters.TripActiveAdapter;
import com.example.strip.Adapters.TripTwoAdapter;
import com.example.strip.Models.Response.TripActiveResponse;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageTripsFragment extends Fragment {
    private RecyclerView recyclerViewTrips;
    private TripTwoAdapter tripAdapter;
    private RecyclerView recyclerView;
    private TripActiveAdapter tripActiveAdapter;
    private List<TripActiveResponse> tripList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_trips, container, false);
        ImageView btnAdd = view.findViewById(R.id.addTripButton);
        recyclerView = view.findViewById(R.id.recyclerView);
        tripActiveAdapter = new TripActiveAdapter(getContext(), tripList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tripActiveAdapter);
        fetchTrips();
        // Find the ImageView by its ID
        // Set an OnClickListener for the ImageView
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                Intent intent = new Intent(getContext(), AddTripActivity.class);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void fetchTrips() {
        ITripMobileApiService service = ApiClient.getClientWithToken(getContext()).create(ITripMobileApiService.class);
        Call<List<TripActiveResponse>> call = service.getActiveTrips(0, 20);

        call.enqueue(new Callback<List<TripActiveResponse>>() {
            @Override
            public void onResponse(Call<List<TripActiveResponse>> call, Response<List<TripActiveResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tripList.clear();
                    tripList.addAll(response.body());
                    tripActiveAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<TripActiveResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load trips", Toast.LENGTH_SHORT).show();
            }
        });
    }
}