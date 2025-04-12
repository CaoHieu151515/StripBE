package com.example.strip.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.strip.Models.DriverVehicleDTO;
import com.example.strip.R;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private List<DriverVehicleDTO> vehicleList;

    public VehicleAdapter(List<DriverVehicleDTO> vehicleList) {
        this.vehicleList = vehicleList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        DriverVehicleDTO vehicle = vehicleList.get(position);
        holder.tvVehicleType.setText(vehicle.getVehicleType());
        holder.tvVehicleBrand.setText(vehicle.getVehicleBrand());
        holder.tvVehicleColor.setText(vehicle.getVehicleColor());
        holder.tvVehicleNumber.setText(vehicle.getVehicleNumber());
        holder.tvStatus.setText(vehicle.getStatus());

        // Load vehicle image
        String imageUrl = vehicle.getVehicleImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("http://localhost")) {
                imageUrl = imageUrl.replace("http://localhost", "http://10.0.2.2");
            }
            Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.ivVehicleImage);
        }
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public void setVehicleList(List<DriverVehicleDTO> newList) {
        this.vehicleList = newList;
        notifyDataSetChanged();
    }

    public static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView tvVehicleType, tvVehicleBrand, tvVehicleColor, tvVehicleNumber, tvStatus;
        ImageView ivVehicleImage;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVehicleType = itemView.findViewById(R.id.tvVehicleType);
            tvVehicleBrand = itemView.findViewById(R.id.tvVehicleBrand);
            tvVehicleColor = itemView.findViewById(R.id.tvVehicleColor);
            tvVehicleNumber = itemView.findViewById(R.id.tvVehicleNumber);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivVehicleImage = itemView.findViewById(R.id.ivVehicleImage);
        }
    }
}

