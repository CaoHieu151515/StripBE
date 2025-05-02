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
import com.example.strip.Activities.Trip.TripActiveDetailActivity;
import com.example.strip.Models.Response.TripActiveResponse;
import com.example.strip.Models.Trip;
import com.example.strip.R;
import com.example.strip.Utils.DateFormatter;

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
        holder.codeTrip.setText("Mã chuyến đi: " + trip.getStripID());
        holder.startLocation.setText("Từ: " + trip.getStartLocation());
        holder.endLocation.setText("Đến: " + trip.getEndlocation());
        holder.price.setText("Giá cả: "+trip.getPrice());
        holder.status.setText("Trạng thái chuyến đi: "+trip.getStatus());
        holder.startDate.setText("Ngày bắt đầu: "+DateFormatter.formatDate(trip.getStartDay()));
        holder.endDate.setText("Ngày kết thúc: " + DateFormatter.formatDate(trip.getEndDay()));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TripActiveDetailActivity.class);
            intent.putExtra("tripId", trip.getStripID()); // Pass tripId to detail activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView startLocation, endLocation, price, status, startDate, endDate, codeTrip;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            codeTrip = itemView.findViewById(R.id.codeTrip);
            startLocation = itemView.findViewById(R.id.startLocation);
            endLocation = itemView.findViewById(R.id.endLocation);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
        }
    }
}
