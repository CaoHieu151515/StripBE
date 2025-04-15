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
import com.example.strip.Models.Response.ConfirmDriverResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConfirmDriverThreeActivity extends AppCompatActivity {
    private EditText etVehicleType, etVehicleColor, etVehicleNumber, etSeats, etVehicleBrand, etVehicleStatus;
    private ImageView vehicleImageView, carRegistrationImageView, inspectionCertificateImageView, insuranceImageView;
    private Button btnConfirmDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_driver_three);
        etVehicleType = findViewById(R.id.etVehicleType);
        etVehicleColor = findViewById(R.id.etVehicleColor);
        etVehicleNumber = findViewById(R.id.etVehicleNumber);
        etSeats = findViewById(R.id.etSeats);
        etVehicleBrand = findViewById(R.id.etVehicleBrand);
        etVehicleStatus = findViewById(R.id.etVehicleStatus);
        vehicleImageView = findViewById(R.id.vehicleImageView);
        carRegistrationImageView = findViewById(R.id.carRegistrationImageView);
        inspectionCertificateImageView = findViewById(R.id.inspectionCertificateImageView);
        insuranceImageView = findViewById(R.id.insuranceImageView);
        btnConfirmDriver = findViewById(R.id.btnConfirmDriver);
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
                    etVehicleType.setText("" + data.getVehicleResponse().getVehicleType());
                    etVehicleColor.setText("" + data.getVehicleResponse().getVehicleColor());
                    etVehicleNumber.setText("" + data.getVehicleResponse().getVehicleNumber());
                    etSeats.setText("" + data.getVehicleResponse().getNumberOfSeats());
                    etVehicleBrand.setText("" + data.getVehicleResponse().getVehicleBrand());
                    etVehicleStatus.setText("" + data.getVehicleResponse().getStatus());

                    // ✅ Load ảnh đơn giản hơn nhiều
                    loadImageWithFixHost(data.getDriverLicenseUrl(), vehicleImageView);
                    loadImageWithFixHost(data.getDriverLicenseUrl(), carRegistrationImageView);
                    loadImageWithFixHost(data.getDriverLicenseUrl(), inspectionCertificateImageView);
                    loadImageWithFixHost(data.getDriverLicenseUrl(), insuranceImageView);
                } else {
                    Toast.makeText(ConfirmDriverThreeActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConfirmDriverResponse> call, Throwable t) {
                Toast.makeText(ConfirmDriverThreeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
