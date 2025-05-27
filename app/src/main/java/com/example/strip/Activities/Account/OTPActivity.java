package com.example.strip.Activities.Account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strip.Activities.StripActivity;
import com.example.strip.Models.Request.OtpVM;
import com.example.strip.Models.Request.RegisterVM;
import com.example.strip.Models.Response.ResponseMessage;
import com.example.strip.R;
import com.example.strip.Services.IAccountApiService;
import com.example.strip.Utils.ErrorTranslate;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPActivity extends AppCompatActivity {
    private EditText edOtp;
    private NotificationPopup notificationPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        // Find the ImageView by its ID
        Button btnVerify = findViewById(R.id.verifyButton);
        notificationPopup = new NotificationPopup(this);

        edOtp = findViewById(R.id.otpInput);
        // Set an OnClickListener for the ImageView
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp();
                // Navigate to CustomerHomeActivity
            }
        });
    }
    private void sendOtp() {
        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        String phone = intent.getStringExtra("phone");

        String otp = edOtp.getText().toString();
        OtpVM request = new OtpVM(email, password, login,true, otp, phone);

        IAccountApiService apiService = ApiClient.getClient().create(IAccountApiService.class);
        Call<Void> call = apiService.verify(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    notificationPopup.showPopup("Gửi OTP thành công!", false);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(OTPActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }, 1500);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Gửi OTP thất bại: " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Gửi OTP thất bại: \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Gửi OTP thất bại: \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);            }
        });
    }
}
