package com.example.strip.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Models.Request.FeedbackRequest;
import com.example.strip.Models.Response.StopLocationBookingResponse;
import com.example.strip.Models.Response.StopLocationDoneResponse;
import com.example.strip.Models.Response.TripBookingResponse;
import com.example.strip.Models.Response.TripDoneResponse;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Utils.DateFormatter;
import com.example.strip.Utils.ErrorTranslate;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.Utils.TripStatusTranslate;
import com.example.strip.network.ApiClient;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDoneAdapter extends RecyclerView.Adapter<TripDoneAdapter.TripViewHolder>{
    private Context context;

    private List<TripDoneResponse> tripDoneResponseList;
    private NotificationPopup notificationPopup;
    public TripDoneAdapter(Context context, List<TripDoneResponse> tripDoneResponseList) {
        this.context = context;
        this.tripDoneResponseList = tripDoneResponseList;
    }

    @NonNull
    @Override
    public TripDoneAdapter.TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip_2, parent, false);
        return new TripDoneAdapter.TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripDoneAdapter.TripViewHolder holder, int position) {
        TripDoneResponse trip = tripDoneResponseList.get(position);
        holder.tvHandleTripId.setText("Mã chuyến đi: " +trip.tripHandleID);
        holder.tvStartLocation.setText("Từ: " + trip.startLocation);
        holder.tvEndLocation.setText("Đến: " + trip.endLocation);
        holder.tvDriver.setText("Tài xế: " + trip.driverName);
        holder.tvStartDate.setText("Thời gian khởi hành: " + DateFormatter.formatDate(trip.startDate));
        holder.tvEndDate.setText("Thời gian kết thúc: " + DateFormatter.formatDate(trip.endDate));
        holder.tvStatus.setText("Trạng thái chuyến đi: "+ TripStatusTranslate.translateStatus(trip.tripStatus));
        holder.btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback(trip.tripID);
            }
        });
        holder.stopLocationContainer.removeAllViews();
        if (trip.stopLocationDoneResponseList != null && !trip.stopLocationDoneResponseList.isEmpty()) {
            // Sắp xếp theo stoplocaPosition tăng dần
            Collections.sort(trip.stopLocationDoneResponseList, new Comparator<StopLocationDoneResponse>() {
                @Override
                public int compare(StopLocationDoneResponse o1, StopLocationDoneResponse o2) {
                    return Integer.compare(o1.stoplocaPosition, o2.stoplocaPosition);
                }
            });

            for (StopLocationDoneResponse stop : trip.stopLocationDoneResponseList) {
                TextView stopView = new TextView(holder.itemView.getContext());
                stopView.setText("Điểm dừng " + stop.stoplocaPosition + ": " + stop.stopLoca +
                        " \n(" + String.format("%.2f km, ~ ", stop.estimatedKM) + String.format("%d phút)", stop.estimatedTime));
                stopView.setPadding(16, 8, 16, 8);
                holder.stopLocationContainer.addView(stopView);
            }
        }

    }

    @Override
    public int getItemCount() {
        return tripDoneResponseList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tvStartLocation, tvEndLocation, tvDriver, tvStartDate, tvEndDate, tvStatus, tvHandleTripId;
        LinearLayout stopLocationContainer;
        Button btnFeedback;
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStartLocation = itemView.findViewById(R.id.tvStartLocation);
            tvEndLocation = itemView.findViewById(R.id.tvEndLocation);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            stopLocationContainer = itemView.findViewById(R.id.stopLocationContainer);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnFeedback = itemView.findViewById(R.id.btnFeedback);
            tvHandleTripId = itemView.findViewById(R.id.tvHandleTripId);
        }
    }
    private void feedback(String tripId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_feedback, null);
        builder.setView(dialogView);

        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        EditText etFeedback = dialogView.findViewById(R.id.etFeedback);

        builder.setTitle("Gửi phản hồi tài xế")
                .setPositiveButton("Gửi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int rating = (int) ratingBar.getRating();
                        String feedbackText = etFeedback.getText().toString();

                        FeedbackRequest request = new FeedbackRequest(feedbackText, rating);
                        sendFeedbackToDriver(tripId, request);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void sendFeedbackToDriver(String tripId, FeedbackRequest request) {
        ITripMobileApiService service = ApiClient.getClientWithToken(context).create(ITripMobileApiService.class);
        Call<ResponseBody> call = service.sendFeedbackToDriver(tripId, request);
        notificationPopup = new NotificationPopup(context);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    notificationPopup.showPopup("Gửi phản hồi thành công!", false);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Không thể gửi phản hồi. " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Không thể gửi phản hồi. Mã lỗi: \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Không thể gửi phản hồi. \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }

}
