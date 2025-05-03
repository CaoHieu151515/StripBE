package com.example.strip.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.strip.Adapters.TripBookingAdapter;
import com.example.strip.Adapters.TripDoneAdapter;
import com.example.strip.Models.Response.TripBookingResponse;
import com.example.strip.Models.Response.TripDoneResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripsFragment extends Fragment {
    private RecyclerView recyclerView, recyclerViewTripsDone;
    private TripBookingAdapter adapter;
    private TripDoneAdapter adapterDone;
    private List<TripBookingResponse> tripList = new ArrayList<>();
    private List<TripDoneResponse> tripDoneResponseList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTrips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TripBookingAdapter(tripList);
        recyclerView.setAdapter(adapter);
        fetchTripsWaiting();

        recyclerViewTripsDone = view.findViewById(R.id.recyclerViewTripsDone);
        recyclerViewTripsDone.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDone = new TripDoneAdapter(tripDoneResponseList);
        recyclerViewTripsDone.setAdapter(adapterDone);
        fetchTripsDone();
        // Inflate the layout for this fragment
        return view;
    }
    private void fetchTripsWaiting() {
        IUserMobileApiService tripService = ApiClient.getClientWithToken(getContext()).create(IUserMobileApiService.class);
        Call<List<TripBookingResponse>> call = tripService.getBookedTrips();
        call.enqueue(new Callback<List<TripBookingResponse>>() {
            @Override
            public void onResponse(Call<List<TripBookingResponse>> call, Response<List<TripBookingResponse>> response) {
                if (response.isSuccessful()) {
                    tripList.clear();
                    tripList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TripBookingResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchTripsDone() {
        IUserMobileApiService tripService = ApiClient.getClientWithToken(getContext()).create(IUserMobileApiService.class);
        Call<List<TripDoneResponse>> call = tripService.getDoneTrips();
        call.enqueue(new Callback<List<TripDoneResponse>>() {
            @Override
            public void onResponse(Call<List<TripDoneResponse>> call, Response<List<TripDoneResponse>> response) {
                if (response.isSuccessful()) {
                    tripDoneResponseList.clear();
                    tripDoneResponseList.addAll(response.body());
                    adapterDone.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TripDoneResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}