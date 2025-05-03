package com.example.strip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Models.StopLocation;
import com.example.strip.R;
import com.example.strip.Utils.DateFormatter;

import java.util.List;

public class TripStopTwoAdapter extends RecyclerView.Adapter<TripStopTwoAdapter.TripStopViewHolder> {

    private List<StopLocation> stopLocations;
    private Context context;

    private final OnStopClickListener listener;

    public TripStopTwoAdapter(Context context, List<StopLocation> stopLocations, OnStopClickListener listener) {
        this.context = context;
        this.stopLocations = stopLocations;
        this.listener = listener;
    }
    public List<StopLocation> getStopList() {
        return stopLocations;
    }

    @NonNull
    @Override
    public TripStopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trip_stop_location_item2, parent, false);
        return new TripStopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripStopViewHolder holder, int position) {
        StopLocation stopLocation = stopLocations.get(position);
        holder.tvStopLocaTime.setText(DateFormatter.formatDate(stopLocation.getStopLocaTime()) + "");
        holder.tvStopLoca.setText(stopLocation.getStopLoca());
        holder.tvStopLocaStatus.setText(stopLocation.getStopLocaStatus());
        holder.tvEstimatedTime.setText("Khoảng thời gian: " + stopLocation.getEstimatedTime() + " mins");
        holder.tvEstimatedKM.setText("Khoảng cách: " + stopLocation.getEstimatedKM() + " km");
        holder.tvPosition.setText("" + stopLocation.getTripPositon());
        holder.itemView.setOnClickListener(v -> {
            listener.onStopClick(stopLocation); // 'stop' is your current StopLocation item
        });
    }

    @Override
    public int getItemCount() {
        return stopLocations.size();
    }

    public static class TripStopViewHolder extends RecyclerView.ViewHolder {
        TextView tvStopLocaTime, tvStopLoca, tvStopLocaStatus, tvEstimatedTime, tvEstimatedKM, tvPosition;
        public TripStopViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStopLocaTime = itemView.findViewById(R.id.tvStopLocaTime);
            tvStopLoca = itemView.findViewById(R.id.tvStopLoca);
            tvStopLocaStatus = itemView.findViewById(R.id.tvStopLocaStatus);
            tvEstimatedTime = itemView.findViewById(R.id.tvEstimatedTime);
            tvEstimatedKM = itemView.findViewById(R.id.tvEstimatedKM);
            tvPosition = itemView.findViewById(R.id.tvPosition);
        }
    }
    public interface OnStopClickListener {
        void onStopClick(StopLocation stopLocation);
    }
}

