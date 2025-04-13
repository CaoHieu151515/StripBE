package com.example.strip.Activities.Account;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPActivity extends AppCompatActivity {
    private EditText edOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        // Find the ImageView by its ID
        Button btnVerify = findViewById(R.id.verifyButton);

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
        String otp = edOtp.getText().toString();
        OtpVM request = new OtpVM(email, password, login,true, otp);

        IAccountApiService apiService = ApiClient.getClient().create(IAccountApiService.class);
        Call<Void> call = apiService.verify(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(OTPActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(OTPActivity.this, "Gửi OTP thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle API error response
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(OTPActivity.this, "Gửi OTP thất bại: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(OTPActivity.this, "Gửi OTP thất bại: Không thể lấy thông báo lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(OTPActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
