package com.example.strip.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Activities.Trip.EditTripActivity;
import com.example.strip.Models.Request.NotificationRequest;
import com.example.strip.Models.Response.RequestTripResponse;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.DateFormatter;
import com.example.strip.Utils.ErrorTranslate;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.Utils.TripStatusTranslate;
import com.example.strip.network.ApiClient;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TripRequestAdapter extends RecyclerView.Adapter<TripRequestAdapter.ViewHolder> {
    private OnTripActionListener listener;

    private Context context;

    private List<RequestTripResponse> requestTripList;
    private NotificationPopup notificationPopup;
    private UserMoreResponse user;
    public TripRequestAdapter(Context context, List<RequestTripResponse> requestTripList, OnTripActionListener listener) {
        this.context = context;
        this.requestTripList = requestTripList;
        this.listener = listener;
    }

    public void setData(List<RequestTripResponse> list) {
        this.requestTripList = list;
        notifyDataSetChanged();
    }
    public interface OnTripActionListener {
        void onActionCompleted();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtStartLocation, txtEndLocation, txtStatus;
        TextView txtLuggage, txtFee, txtPickupTime, txtCheckIn, txtCheckOut;
        Button btnCheckIn, btnCheckOut, btnAccept, btnReject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStartLocation = itemView.findViewById(R.id.txtStartLocation);
            txtEndLocation = itemView.findViewById(R.id.txtEndLocation);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtLuggage = itemView.findViewById(R.id.txtLuggage);
            txtFee = itemView.findViewById(R.id.txtFee);
            txtPickupTime = itemView.findViewById(R.id.txtPickupTime);
            txtCheckIn = itemView.findViewById(R.id.txtCheckIn);
            txtCheckOut = itemView.findViewById(R.id.txtCheckOut);

            btnCheckIn = itemView.findViewById(R.id.btnCheckIn);
            btnCheckOut = itemView.findViewById(R.id.btnCheckOut);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    @NonNull
    @Override
    public TripRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request_trip, parent, false);
        fetchUserInfo();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripRequestAdapter.ViewHolder holder, int position) {
        RequestTripResponse trip = requestTripList.get(position);
        holder.txtStartLocation.setText("Bắt đầu: " + trip.startLoca.stopLoca);
        holder.txtEndLocation.setText("Kết thúc: " + trip.endLoca.stopLoca);
        holder.txtStatus.setText("Trạng thái : " + TripStatusTranslate.translateStatus(trip.status));
        holder.txtLuggage.setText("Hành lý: " + trip.luggageDescription);
        holder.txtFee.setText("Phí: " + trip.amountApproveFee + " VND");

        String pickUp = trip.pickUpTime != null ? DateFormatter.formatDate(trip.pickUpTime) : "";
        String checkIn = trip.checkInTime != null ? DateFormatter.formatDate(trip.checkInTime) : "";
        String checkOut = trip.checkOutTIme != null ? DateFormatter.formatDate(trip.checkOutTIme) : "";
        holder.txtCheckIn.setText("Lên xe: " + checkIn);
        holder.txtCheckOut.setText("Xuống xe: " + checkOut);
        holder.txtPickupTime.setText("Thời gian đón: " + pickUp);

        holder.btnAccept.setOnClickListener(v -> {
            // Call API to accept trip
            acceptTrip(trip.requestTripID);
        });

        holder.btnReject.setOnClickListener(v -> {
            // Call API to reject trip
            rejectTrip(trip.requestTripID);
        });

        holder.btnCheckIn.setOnClickListener(v -> {
            // Call API to check in
            checkInTrip(trip.requestTripID);
        });

        holder.btnCheckOut.setOnClickListener(v -> {
            // Call API to check out
            checkOutTrip(trip.requestTripID);
        });
    }

    @Override
    public int getItemCount() {
        return requestTripList == null ? 0 : requestTripList.size();
    }

    private void acceptTrip(String requestTripId) {
        String userId = user.getDriver().getUserId();
        ITripMobileApiService tripService = ApiClient.getClientWithToken(context).create(ITripMobileApiService.class);
        Call<ResponseBody> call = tripService.acceptRequestTrip(requestTripId);
        notificationPopup = new NotificationPopup(context);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (listener != null) {
                        listener.onActionCompleted(); // trigger reload
                    }
                    // Success, you can handle it here
                    try {
                        NotificationRequest notiRequest = new NotificationRequest(
                                "Bạn đã chấp nhận yêu cầu chuyến đi thành công!", // title or message
                                "Mã yêu cầu chuyến đi: " + requestTripId, // detailed message
                                userId // or other target
                        );
                        notificationPopup.createNotification(notiRequest);
                        notificationPopup.showPopup("Chấp nhận chuyến đi thành công!", false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Failed", "Failed to accept trip!" + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Không chấp nhận được chuyến đi! " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Không chấp nhận được chuyến đi! \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Không chấp nhận được chuyến đi! \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Network error, server unreachable, etc.
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }

    private void rejectTrip(String requestTripId) {
        String userId = user.getDriver().getUserId();
        ITripMobileApiService tripService = ApiClient.getClientWithToken(context).create(ITripMobileApiService.class);
        Call<ResponseBody> call = tripService.rejectRequestTrip(requestTripId);
        notificationPopup = new NotificationPopup(context);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (listener != null) {
                        listener.onActionCompleted(); // trigger reload
                    }
                    // Success, you can handle it here
                    try {
                        NotificationRequest notiRequest = new NotificationRequest(
                                "Bạn đã từ chối yêu cầu chuyến đi thành công!", // title or message
                                "Mã yêu cầu chuyến đi: " + requestTripId, // detailed message
                                userId // or other target
                        );
                        notificationPopup.createNotification(notiRequest);
                        // Show success or do something
                        notificationPopup.showPopup("Từ chối chuyến đi thành công!", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Request failed but got a response from server (e.g., 400, 404, etc.)
                    Log.e("Failed", "Failed to reject trip!" + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Không từ chối được chuyến đi! " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Không từ chối được chuyến đi! \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Không từ chối được chuyến đi! \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Network error, server unreachable, etc.
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }

    private void checkInTrip(String requestTripId) {
        String userId = user.getDriver().getUserId();
        ITripMobileApiService tripService = ApiClient.getClientWithToken(context).create(ITripMobileApiService.class);
        Call<ResponseBody> call = tripService.checkInRequestTrip(requestTripId);
        notificationPopup = new NotificationPopup(context);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (listener != null) {
                        listener.onActionCompleted(); // trigger reload
                    }
                    // Success, you can handle it here
                    try {
                        String responseBody = response.body().string();
                        NotificationRequest notiRequest = new NotificationRequest(
                                "Bạn đã cho lên xe với yêu cầu chuyến đi thành công!", // title or message
                                "Mã yêu cầu chuyến đi: " + requestTripId, // detailed message
                                userId // or other target
                        );
                        notificationPopup.createNotification(notiRequest);
                        // Show success or do something
                        notificationPopup.showPopup("Cho lên xe thành công!", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Request failed but got a response from server (e.g., 400, 404, etc.)
                    Log.e("Failed", "Failed to checkIn trip!" + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Không cho lên xe được! " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Không cho lên xe được! \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Không cho lên xe được! \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Network error, server unreachable, etc.
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }

    private void checkOutTrip(String requestTripId) {
        String userId = user.getDriver().getUserId();
        ITripMobileApiService tripService = ApiClient.getClientWithToken(context).create(ITripMobileApiService.class);
        Call<ResponseBody> call = tripService.checkOutRequestTrip(requestTripId);
        notificationPopup = new NotificationPopup(context);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (listener != null) {
                        listener.onActionCompleted(); // trigger reload
                    }
                    // Success, you can handle it here
                    try {
                        NotificationRequest notiRequest = new NotificationRequest(
                                "Bạn đã cho xuống xe với yêu cầu chuyến đi thành công!", // title or message
                                "Mã yêu cầu chuyến đi: " + requestTripId, // detailed message
                                userId // or other target
                        );
                        notificationPopup.createNotification(notiRequest);
                        // Show success or do something
                        notificationPopup.showPopup("Cho xuống xe thành công!", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    // Request failed but got a response from server (e.g., 400, 404, etc.)
                    Log.e("CheckOut", "Failed: " + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Không cho xuống xe được! " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Không cho xuống xe được! \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Không cho xuống xe được! \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Network error, server unreachable, etc.
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }
    private void fetchUserInfo() {
        Retrofit retrofit = ApiClient.getClientWithToken(context);
        IUserMobileApiService apiService = retrofit.create(IUserMobileApiService.class);
        Call<UserMoreResponse> call = apiService.getUserInfo();

        call.enqueue(new Callback<UserMoreResponse>() {
            @Override
            public void onResponse(Call<UserMoreResponse> call, Response<UserMoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                } else {
                    try {
                        String errorBody = response.errorBody().string();

                        Log.e("Error", "Lỗi khi lấy thông tin: " + errorBody);
                        Toast.makeText(context, "Lỗi khi lấy thông tin: " + response.code(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Lỗi khi lấy thông tin: \nKhông thể lấy thông báo lỗi\n" + response.code(), true);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserMoreResponse> call, Throwable t) {
                Log.e("Error", "Lỗi khi gọi API: " + t.getMessage(), t);
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }
}

