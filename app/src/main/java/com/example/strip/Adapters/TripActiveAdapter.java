package com.example.strip.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Activities.Trip.EditTripActivity;
import com.example.strip.Models.Response.TripActiveResponse;
import com.example.strip.Models.Trip;
import com.example.strip.R;

import java.util.List;

public class TripActiveAdapter extends RecyclerView.Adapter<TripActiveAdapter.TripViewHolder>{
    private Context context;

    private List<TripActiveResponse> tripList;

    public TripActiveAdapter(Context context, List<TripActiveResponse> tripList) {
        this.context = context;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_active, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripActiveResponse trip = tripList.get(position);
        holder.startLocation.setText(trip.getStartLocation());
        holder.endLocation.setText(trip.getEndlocation());
        holder.price.setText(String.valueOf(trip.getPrice()));
        holder.status.setText(trip.getStatus());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTripActivity.class);
            intent.putExtra("tripId", trip.getStripID()); // Pass tripId to detail activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView startLocation, endLocation, price, status;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            startLocation = itemView.findViewById(R.id.startLocation);
            endLocation = itemView.findViewById(R.id.endLocation);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
        }
    }
}
