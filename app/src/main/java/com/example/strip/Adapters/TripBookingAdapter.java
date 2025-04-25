package com.example.strip.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Models.Response.StopLocationBookingResponse;
import com.example.strip.Models.Response.TripBookingResponse;
import com.example.strip.R;
import com.example.strip.Utils.DateFormatter;

import java.util.List;

public class TripBookingAdapter extends RecyclerView.Adapter<TripBookingAdapter.TripViewHolder>{
    private List<TripBookingResponse> tripBookingResponseList;

    public TripBookingAdapter(List<TripBookingResponse> tripBookingResponseList) {
        this.tripBookingResponseList = tripBookingResponseList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_3, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripBookingResponse trip = tripBookingResponseList.get(position);
        holder.tvStartLocation.setText("From: " + trip.startLocation);
        holder.tvEndLocation.setText("To: " + trip.endLocation);
        holder.tvDriver.setText("Driver: " + trip.driverName);
        holder.tvStartDate.setText("Start Date: " + DateFormatter.formatDate(trip.startDate));
        holder.tvEndDate.setText("End Date: " + DateFormatter.formatDate(trip.endDate));
        holder.stopLocationContainer.removeAllViews();

        if (trip.stopLocationBookingResponseList != null && !trip.stopLocationBookingResponseList.isEmpty()) {
            for (StopLocationBookingResponse stop : trip.stopLocationBookingResponseList) {
                TextView stopView = new TextView(holder.itemView.getContext());
                stopView.setText("Stop " + stop.stoplocaPosition + ": " + stop.stopLoca +
                        " \n(" + String.format("%.2f km, ~", stop.estimatedKM) + String.format("%d mins)", stop.estimatedTime));
                stopView.setPadding(16, 8, 16, 8);
                holder.stopLocationContainer.addView(stopView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return tripBookingResponseList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tvStartLocation, tvEndLocation, tvDriver, tvStartDate, tvEndDate;
        LinearLayout stopLocationContainer;
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStartLocation = itemView.findViewById(R.id.tvStartLocation);
            tvEndLocation = itemView.findViewById(R.id.tvEndLocation);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            stopLocationContainer = itemView.findViewById(R.id.stopLocationContainer);
        }
    }
}
