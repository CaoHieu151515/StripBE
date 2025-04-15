package com.example.strip.Activities.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.strip.Activities.Customer.ConfirmDriverActivity;
import com.example.strip.Models.Response.ConfirmDriverResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConfirmDriverOneActivity extends AppCompatActivity {
    private EditText etFullName, etLastName, etPhone, etEmail;
    private ImageView licenseImageView;
    private Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_driver_one);
        etFullName = findViewById(R.id.etFullName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        licenseImageView = findViewById(R.id.licenseImageView);
        btnNext = findViewById(R.id.btnNext);

        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService userService = retrofit.create(IUserMobileApiService.class);

        Call<ConfirmDriverResponse> call = userService.getConfirmDriver();

        call.enqueue(new Callback<ConfirmDriverResponse>() {
            @Override
            public void onResponse(Call<ConfirmDriverResponse> call, Response<ConfirmDriverResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ConfirmDriverResponse data = response.body();
                    etFullName.setText("" + data.getFirstName());
                    etLastName.setText("" + data.getLastName());
                    etPhone.setText("" + data.getPhone());
                    etEmail.setText("" + data.getEmail());
                    // ✅ Load ảnh đơn giản hơn nhiều
                    loadImageWithFixHost(data.getDriverLicenseUrl(), licenseImageView);
                } else {
                    Toast.makeText(ConfirmDriverOneActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConfirmDriverResponse> call, Throwable t) {
                Toast.makeText(ConfirmDriverOneActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConfirmDriverTwoActivity.class);

            startActivity(intent);
        });
    }
    private void loadImageWithFixHost(String url, ImageView target) {
        if (url != null && !url.isEmpty()) {
            if (url.contains("localhost")) {
                url = url.replace("http://localhost", "http://10.0.2.2:8080");
            }

            Log.d("ImageDebug", "Image URL: " + url);

            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logout)
                    .into(target);
        }
    }
}
