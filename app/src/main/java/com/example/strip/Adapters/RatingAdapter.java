package com.example.strip.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.strip.Models.Response.RatingOfDriverInfoResponse;
import com.example.strip.R;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {
    private List<RatingOfDriverInfoResponse> ratingList;

    public RatingAdapter(List<RatingOfDriverInfoResponse> ratingList) {
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        RatingOfDriverInfoResponse rating = ratingList.get(position);
        holder.txtUser.setText(rating.userName);
        holder.txtFeedback.setText("Phản hồi: " + rating.feedbackContent);
        holder.txtValue.setText("Xếp hạng: " + rating.ratingValue);
        Glide.with(holder.itemView.getContext()).load(rating.avatarUser).into(holder.imgUser);
    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    class RatingViewHolder extends RecyclerView.ViewHolder {
        TextView txtUser, txtFeedback, txtValue;
        ImageView imgUser;

        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.txtUser);
            txtFeedback = itemView.findViewById(R.id.txtFeedback);
            txtValue = itemView.findViewById(R.id.txtValue);
            imgUser = itemView.findViewById(R.id.imgUser);
        }
    }
}

