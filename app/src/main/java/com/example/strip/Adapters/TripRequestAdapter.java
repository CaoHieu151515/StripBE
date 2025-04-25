package com.example.strip.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Models.Response.RequestTripResponse;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Utils.DateFormatter;
import com.example.strip.network.ApiClient;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripRequestAdapter extends RecyclerView.Adapter<TripRequestAdapter.ViewHolder> {
    private OnTripActionListener listener;

    private Context context;

    private List<RequestTripResponse> requestTripList;

    public TripRequestAdapter(Context context, List<RequestTripResponse> requestTripList, OnTripActionListener listener) {
        this.context = context;
        this.requestTripList = requestTripList;
        this.listener = listener;
    }

    public void setData(List<RequestTripResponse> list) {
        this.requestTripList = list;
        notifyDataSetChanged();
    }
    public interface OnTripActionListener {
        void onActionCompleted();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtStartLocation, txtEndLocation, txtStatus;
        TextView txtLuggage, txtFee, txtPickupTime, txtCheckIn, txtCheckOut;
        Button btnCheckIn, btnCheckOut, btnAccept, btnReject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStartLocation = itemView.findViewById(R.id.txtStartLocation);
            txtEndLocation = itemView.findViewById(R.id.txtEndLocation);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtLuggage = itemView.findViewById(R.id.txtLuggage);
            txtFee = itemView.findViewById(R.id.txtFee);
            txtPickupTime = itemView.findViewById(R.id.txtPickupTime);
            txtCheckIn = itemView.findViewById(R.id.txtCheckIn);
            txtCheckOut = itemView.findViewById(R.id.txtCheckOut);

            btnCheckIn = itemView.findViewById(R.id.btnCheckIn);
            btnCheckOut = itemView.findViewById(R.id.btnCheckOut);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    @NonNull
    @Override
    public TripRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripRequestAdapter.ViewHolder holder, int position) {
        RequestTripResponse trip = requestTripList.get(position);
        holder.txtStartLocation.setText("Start: " + trip.startLoca.stopLoca);
        holder.txtEndLocation.setText("End: " + trip.endLoca.stopLoca);
        holder.txtStatus.setText("Status: " + trip.status);
        holder.txtLuggage.setText("Luggage: " + trip.luggageDescription);
        holder.txtFee.setText("Fee: " + trip.amountApproveFee + " VND");

        String pickUp = trip.pickUpTime != null ? DateFormatter.formatDate(trip.pickUpTime) : "N/A";
        String checkIn = trip.checkInTime != null ? DateFormatter.formatDate(trip.checkInTime) : "N/A";
        String checkOut = trip.checkOutTIme != null ? DateFormatter.formatDate(trip.checkOutTIme) : "N/A";
        holder.txtCheckIn.setText("Check-In: " + checkIn);
        holder.txtCheckOut.setText("Check-Out: " + checkOut);
        holder.txtPickupTime.setText("Pickup: " + pickUp);

        holder.btnAccept.setOnClickListener(v -> {
            // Call API to accept trip
            acceptTrip(trip.requestTripID);
        });

        holder.btnReject.setOnClickListener(v -> {
            // Call API to reject trip
            rejectTrip(trip.requestTripID);
        });

        holder.btnCheckIn.setOnClickListener(v -> {
            // Call API to check in
            checkInTrip(trip.requestTripID);
        });

        holder.btnCheckOut.setOnClickListener(v -> {
            // Call API to check out
            checkOutTrip(trip.requestTripID);
        });
    }

    @Override
    public int getItemCount() {
        return requestTripList == null ? 0 : requestTripList.size();
    }

    private void acceptTrip(String requestTripId) {
        ITripMobileApiService tripService = ApiClient.getClientWithToken(context).create(ITripMobileApiService.class);
        Call<ResponseBody> call = tripService.acceptRequestTrip(requestTripId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (listener != null) {
                        listener.onActionCompleted(); // trigger reload
                    }
                    // Success, you can handle it here
                    try {
                        String responseBody = response.body().string();
                        // Show success or do something
                        Log.d("AcceptTrip", "Success: " + responseBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Request failed but got a response from server (e.g., 400, 404, etc.)
                    Log.e("AcceptTrip", "Failed: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Network error, server unreachable, etc.
                Log.e("AcceptTrip", "Error: " + t.getMessage());
            }
        });
    }

    private void rejectTrip(String requestTripId) {
        ITripMobileApiService tripService = ApiClient.getClientWithToken(context).create(ITripMobileApiService.class);
        Call<ResponseBody> call = tripService.rejectRequestTrip(requestTripId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (listener != null) {
                        listener.onActionCompleted(); // trigger reload
                    }
                    // Success, you can handle it here
                    try {
                        String responseBody = response.body().string();
                        // Show success or do something
                        Log.d("RejectTrip", "Success: " + responseBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Request failed but got a response from server (e.g., 400, 404, etc.)
                    Log.e("RejectTrip", "Failed: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Network error, server unreachable, etc.
                Log.e("RejectTrip", "Error: " + t.getMessage());
            }
        });
    }

    private void checkInTrip(String requestTripId) {
        ITripMobileApiService tripService = ApiClient.getClientWithToken(context).create(ITripMobileApiService.class);
        Call<ResponseBody> call = tripService.checkInRequestTrip(requestTripId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (listener != null) {
                        listener.onActionCompleted(); // trigger reload
                    }
                    // Success, you can handle it here
                    try {
                        String responseBody = response.body().string();
                        // Show success or do something
                        Log.d("CheckIn", "Success: " + responseBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Request failed but got a response from server (e.g., 400, 404, etc.)
                    Log.e("CheckIn", "Failed: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Network error, server unreachable, etc.
                Log.e("CheckIn", "Error: " + t.getMessage());
            }
        });
    }

    private void checkOutTrip(String requestTripId) {
        ITripMobileApiService tripService = ApiClient.getClientWithToken(context).create(ITripMobileApiService.class);
        Call<ResponseBody> call = tripService.checkOutRequestTrip(requestTripId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (listener != null) {
                        listener.onActionCompleted(); // trigger reload
                    }
                    // Success, you can handle it here
                    try {
                        String responseBody = response.body().string();
                        // Show success or do something
                        Log.d("CheckOut", "Success: " + responseBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Request failed but got a response from server (e.g., 400, 404, etc.)
                    Log.e("CheckOut", "Failed: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Network error, server unreachable, etc.
                Log.e("CheckOut", "Error: " + t.getMessage());
            }
        });
    }
}

