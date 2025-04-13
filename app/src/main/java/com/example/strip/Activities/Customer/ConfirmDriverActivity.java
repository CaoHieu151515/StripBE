package com.example.strip.Activities.Customer;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class ConfirmDriverActivity extends AppCompatActivity {

    private TextView userIdTextView, driverIdTextView, firstNameTextView, lastNameTextView,
            phoneTextView, emailTextView,
            vehicleTypeTextView, vehicleNumberTextView, vehicleBrandTextView,
            vehicleColorTextView, seatsTextView, statusTextView;
    private ImageView faceUpImageView, faceDownImageView, licenseImageView,
            vehicleImageView, carRegistrationImageView, inspectionCertificateImageView, insuranceImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_driver);

        // Initialize views
        userIdTextView = findViewById(R.id.userIdTextView);
        driverIdTextView = findViewById(R.id.driverIdTextView);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        emailTextView = findViewById(R.id.emailTextView);
        faceUpImageView = findViewById(R.id.faceUpImageView);
        faceDownImageView = findViewById(R.id.faceDownImageView);
        licenseImageView = findViewById(R.id.licenseImageView);

        vehicleTypeTextView = findViewById(R.id.vehicleTypeTextView);
        vehicleNumberTextView = findViewById(R.id.vehicleNumberTextView);
        vehicleBrandTextView = findViewById(R.id.vehicleBrandTextView);
        vehicleColorTextView = findViewById(R.id.vehicleColorTextView);
        seatsTextView = findViewById(R.id.seatsTextView);
        statusTextView = findViewById(R.id.statusTextView);

        vehicleImageView = findViewById(R.id.vehicleImageView);
        carRegistrationImageView = findViewById(R.id.carRegistrationImageView);
        inspectionCertificateImageView = findViewById(R.id.inspectionCertificateImageView);
        insuranceImageView = findViewById(R.id.insuranceImageView);


        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Call API
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService userService = retrofit.create(IUserMobileApiService.class);

        Call<ConfirmDriverResponse> call = userService.getConfirmDriver();

        call.enqueue(new Callback<ConfirmDriverResponse>() {
            @Override
            public void onResponse(Call<ConfirmDriverResponse> call, Response<ConfirmDriverResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ConfirmDriverResponse data = response.body();

                    userIdTextView.setText("User ID: " + data.getUserId());
                    driverIdTextView.setText("Driver ID: " + data.getDriverId());
                    firstNameTextView.setText("First Name: " + data.getFirstName());
                    lastNameTextView.setText("Last Name: " + data.getLastName());
                    phoneTextView.setText("Phone: " + data.getPhone());
                    emailTextView.setText("Email: " + data.getEmail());

                    vehicleTypeTextView.setText("Vehicle Type: " + data.getVehicleResponse().getVehicleType());
                    vehicleNumberTextView.setText("Vehicle Number: " + data.getVehicleResponse().getVehicleNumber());
                    vehicleBrandTextView.setText("Vehicle Brand: " + data.getVehicleResponse().getVehicleBrand());
                    vehicleColorTextView.setText("Vehicle Color: " + data.getVehicleResponse().getVehicleColor());
                    seatsTextView.setText("Seats: " + data.getVehicleResponse().getNumberOfSeats());
                    statusTextView.setText("Vehicle Status: " + data.getVehicleResponse().getStatus());

                    // ✅ Load ảnh đơn giản hơn nhiều
                    loadImageWithFixHost(data.getIdentityCardFaceUpUrl(), faceUpImageView);
                    loadImageWithFixHost(data.getIdentityCardFaceDownUrl(), faceDownImageView);
                    loadImageWithFixHost(data.getDriverLicenseUrl(), licenseImageView);
                    loadImageWithFixHost(data.getVehicleResponse().getVehicleImageUrl(), vehicleImageView);
                    loadImageWithFixHost(data.getVehicleResponse().getCarregistrationUrl(), carRegistrationImageView);
                    loadImageWithFixHost(data.getVehicleResponse().getVehicleInspectionCertificateUrl(), inspectionCertificateImageView);
                    loadImageWithFixHost(data.getVehicleResponse().getCarInsuranceUrl(), insuranceImageView);
                } else {
                    Toast.makeText(ConfirmDriverActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConfirmDriverResponse> call, Throwable t) {
                Toast.makeText(ConfirmDriverActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
//    private Retrofit getRetrofitClient() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        String jwtToken = sharedPreferences.getString("jwtToken", null);
//        if (jwtToken == null) {
//            Toast.makeText(ConfirmDriverActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
//        }
//        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
//                .newBuilder()
//                .addInterceptor(chain -> {
//                    Request.Builder requestBuilder = chain.request().newBuilder();
//                    if (jwtToken != null) {
//                        requestBuilder.addHeader("Authorization", "Bearer " + jwtToken);
//                    }
//                    return chain.proceed(requestBuilder.build());
//                })
//                .build();
//
//
//        return new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/")
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }

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
