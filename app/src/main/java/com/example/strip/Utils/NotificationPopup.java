package com.example.strip.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strip.Models.Request.NotificationRequest;
import com.example.strip.R;
import com.example.strip.Services.INotificationApiService;
import com.example.strip.network.ApiClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationPopup {

    private final Context context;

    public NotificationPopup(Context context) {
        this.context = context;
    }

    public void showPopup(String message, boolean isError) {
        // Cast context to Activity to access getWindow()
        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("Context must be an instance of Activity");
        }

        Activity activity = (Activity) context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.popup_notification, null);

        TextView popupMessage = popupView.findViewById(R.id.popupMessage);
        ImageView popupIcon = popupView.findViewById(R.id.popupIcon);
        LinearLayout popupRoot = popupView.findViewById(R.id.popupRoot);

        popupMessage.setText(message);
        if (isError) {
            popupRoot.setBackgroundColor(Color.parseColor("#CCFF4444")); // red for error
            popupIcon.setImageResource(R.drawable.exclamation); // your error icon
        } else {
            popupRoot.setBackgroundColor(Color.parseColor("#CC44FF44")); // green for success
            popupIcon.setImageResource(R.drawable.check_circle); // your success icon
        }

        // Show the popup in the root of the activity layout
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        FrameLayout container = new FrameLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        params.setMargins(0, 600, 0, 0); // adjust vertical offset if needed
        container.addView(popupView);
        rootView.addView(container, params);

        // Auto dismiss popup after 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> rootView.removeView(container), 3000);
    }

    public void createNotification(NotificationRequest notificationRequest) {
        INotificationApiService service = ApiClient.getClientWithToken(context).create(INotificationApiService.class);
        Call<Void> call = service.createNotification(notificationRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showPopup("Tạo thông báo thành công!" , false);
                } else {
                    Log.e("Failed", "Failed to create notifications!" + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Không tạo được thông báo! " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        showPopup("Không tạo được thông báo! \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showPopup("Không tạo được thông báo! \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                showPopup("Lỗi khi tạo thông báo: " + t.getMessage(), true);
            }
        });
    }
}
