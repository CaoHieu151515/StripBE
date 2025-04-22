package com.example.strip.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.strip.Activities.Trip.EditTripActivity;
import com.example.strip.Activities.Trip.TripDetailActivity;
import com.example.strip.Models.Trip;
import com.example.strip.R;

import java.util.List;

public class TripTwoAdapter extends RecyclerView.Adapter<TripTwoAdapter.TripViewHolder> {
    private Context context;
    private List<Trip> tripList;

    public TripTwoAdapter(Context context, List<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.tvStartLocation.setText(trip.getStartLocation());
        holder.tvEndLocation.setText(trip.getEndLocation());
        holder.tvPrice.setText("Price: " + trip.getPricePerSeat());
        holder.tvSeats.setText("Seats: " + trip.getCurrentSeat() + "/" + trip.getMaxSeat());
        holder.tvDriver.setText("Driver: " + trip.getDriverName());
        holder.tvVehicleType.setText("Vehicle: " + trip.getVehicleType());
        String imageUrl = trip.getTripImgUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("https://localhost:8080")) {
                imageUrl = imageUrl.replace("https://localhost:8080", "http://10.0.2.2:8080");
            }
            Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.ivTripImage);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTripActivity.class);
            intent.putExtra("tripId", trip.getTripID()); // Pass tripId to detail activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tvStartLocation, tvEndLocation, tvPrice, tvSeats, tvDriver, tvVehicleType;
        ImageView ivTripImage;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStartLocation = itemView.findViewById(R.id.tvStartLocation);
            tvEndLocation = itemView.findViewById(R.id.tvEndLocation);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvVehicleType = itemView.findViewById(R.id.tvVehicleType);
            ivTripImage = itemView.findViewById(R.id.ivTripImage);
        }
    }
}

