package com.example.strip.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Models.LocationInfo;
import com.example.strip.R;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private List<LocationInfo> locationList;
    private OnItemDeleteListener deleteListener;
    public interface OnItemDeleteListener {
        void onItemDeleted(int position, List<LocationInfo> updatedList);
    }

    public LocationAdapter(List<LocationInfo> locationList, OnItemDeleteListener deleteListener) {
        this.locationList = locationList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_info, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationInfo info = locationList.get(position);
        holder.tvStart.setText("Từ: \n" + info.getStartLocation());
        holder.tvEnd.setText("Đến: \n" + info.getEndLocation());
        holder.tvDistance.setText("Khoảng cách: \n" + info.getDistance());
        holder.tvDuration.setText("Khoảng thời gian: \n" + info.getDuration());
        holder.btnDelete.setOnClickListener(view -> {
            locationList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, locationList.size());
            deleteListener.onItemDeleted(position, locationList);
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }
    public void updateData(List<LocationInfo> newList) {
        locationList.clear();
        locationList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvStart, tvEnd, tvDistance, tvDuration;
        Button btnDelete;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStart = itemView.findViewById(R.id.tvStart);
            tvEnd = itemView.findViewById(R.id.tvEnd);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

