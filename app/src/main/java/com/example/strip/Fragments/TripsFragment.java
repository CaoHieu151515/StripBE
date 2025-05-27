package com.example.strip.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
    private TextView tvLoading1, tvLoading2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);
        tvLoading1 = view.findViewById(R.id.tvLoading1);
        tvLoading2 = view.findViewById(R.id.tvLoading2);

        recyclerView = view.findViewById(R.id.recyclerViewTrips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TripBookingAdapter(getContext(),tripList);
        recyclerView.setAdapter(adapter);

        fetchTripsWaiting();

        recyclerViewTripsDone = view.findViewById(R.id.recyclerViewTripsDone);
        recyclerViewTripsDone.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDone = new TripDoneAdapter(getContext(),tripDoneResponseList);
        recyclerViewTripsDone.setAdapter(adapterDone);
        fetchTripsDone();
        // Inflate the layout for this fragment
        return view;
    }
    private void fetchTripsWaiting() {
        // Show loading
        tvLoading1.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        IUserMobileApiService tripService = ApiClient.getClientWithToken(getContext()).create(IUserMobileApiService.class);
        Call<List<TripBookingResponse>> call = tripService.getBookedTrips();
        call.enqueue(new Callback<List<TripBookingResponse>>() {
            @Override
            public void onResponse(Call<List<TripBookingResponse>> call, Response<List<TripBookingResponse>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    tripList.clear();
                    tripList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    // Data loaded → hide loading text, show list
                    tvLoading1.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    tvLoading1.setText("Failed to load waiting trips.");
                }
            }

            @Override
            public void onFailure(Call<List<TripBookingResponse>> call, Throwable t) {
                if (!isAdded()) return;
                tvLoading1.setText("Error: " + t.getMessage());
            }
        });
    }

    private void fetchTripsDone() {
        // Show loading
        tvLoading2.setVisibility(View.VISIBLE);
        recyclerViewTripsDone.setVisibility(View.GONE);

        IUserMobileApiService tripService = ApiClient.getClientWithToken(getContext()).create(IUserMobileApiService.class);
        Call<List<TripDoneResponse>> call = tripService.getDoneTrips();
        call.enqueue(new Callback<List<TripDoneResponse>>() {
            @Override
            public void onResponse(Call<List<TripDoneResponse>> call, Response<List<TripDoneResponse>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    tripDoneResponseList.clear();
                    tripDoneResponseList.addAll(response.body());
                    adapterDone.notifyDataSetChanged();

                    tvLoading2.setVisibility(View.GONE);
                    recyclerViewTripsDone.setVisibility(View.VISIBLE);
                } else {
                    tvLoading2.setText("Failed to load completed trips.");
                }
            }

            @Override
            public void onFailure(Call<List<TripDoneResponse>> call, Throwable t) {
                if (!isAdded()) return;
                tvLoading2.setText("Error: " + t.getMessage());
            }
        });
    }

}