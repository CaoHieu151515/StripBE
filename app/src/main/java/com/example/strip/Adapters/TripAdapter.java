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
import com.example.strip.Activities.Trip.TripDetailActivity;
import com.example.strip.Models.Trip;
import com.example.strip.R;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private Context context;
    private List<Trip> tripList;

    public TripAdapter(Context context, List<Trip> tripList) {
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
        holder.tvPrice.setText( trip.getPricePerSeat() + " VND");
        holder.tvSeats.setText(trip.getCurrentSeat() + "/" + trip.getMaxSeat() + " Ghế ngồi");
        String imageUrl = trip.getTripImgUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("https://localhost")) {
                imageUrl = imageUrl.replace("https://localhost", "http://10.0.2.2");
            }
            Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.ivTripImage);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TripDetailActivity.class);
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
            ivTripImage = itemView.findViewById(R.id.ivTripImage);
        }
    }
}
