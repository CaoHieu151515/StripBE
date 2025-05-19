package com.example.strip.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Models.Response.NotificationResponse;
import com.example.strip.R;
import com.example.strip.Services.INotificationApiService;
import com.example.strip.Utils.DateFormatter;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationResponse> notificationList;
    private Context context;
    private INotificationApiService apiService;
    private NotificationPopup notificationPopup;
    public NotificationAdapter(Context context, List<NotificationResponse> list) {
        this.context = context;
        this.notificationList = list;
        this.apiService = ApiClient.getClientWithToken(context).create(INotificationApiService.class);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, date;
        ImageView readStatus;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.notificationTitle);
            content = view.findViewById(R.id.notificationContent);
            date = view.findViewById(R.id.notificationDate);
            readStatus = view.findViewById(R.id.notificationReadIcon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationResponse notification = notificationList.get(position);
        holder.title.setText(notification.getTitle());
        holder.content.setText(notification.getContent());
        holder.date.setText(DateFormatter.formatDate(notification.getCreatedDate()));

        // Change icon based on read status
        if (notification.isRead()) {
            holder.readStatus.setImageResource(R.drawable.book); // icon for read
            holder.date.setTextColor(Color.parseColor("#4CAF50")); // ✅ green
        } else {
            holder.readStatus.setImageResource(R.drawable.bell); // icon for unread
            holder.date.setTextColor(Color.parseColor("#FF4433")); // ❌ red
        }
        notificationPopup = new NotificationPopup(context);
        holder.itemView.setOnClickListener(v -> {
            if (!notification.isRead()) {
                // Call PATCH API
                apiService.markAsRead((long) notification.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Mark as read locally
                            notification.setRead(true);
                            notifyItemChanged(holder.getAdapterPosition());
                            notificationPopup.showPopup("Đã đánh dấu là đã đọc",false);
                        } else {
                            notificationPopup.showPopup("Không thể đánh dấu thông báo",false);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("API_ERROR", "Error: " + t.getMessage());
                        notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}

