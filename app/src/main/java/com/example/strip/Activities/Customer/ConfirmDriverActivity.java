package com.example.strip.Activities.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.strip.Models.Response.ConfirmDriverResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.UnsafeOkHttpClient;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        Retrofit retrofit = getRetrofitClient();

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

                    String ivIdentityCardFaceUpUrl = data.getIdentityCardFaceUpUrl();
                    if (ivIdentityCardFaceUpUrl != null && !ivIdentityCardFaceUpUrl.isEmpty()) {
                        if (ivIdentityCardFaceUpUrl.startsWith("http://localhost")) {
                            ivIdentityCardFaceUpUrl = ivIdentityCardFaceUpUrl.replace("http://localhost", "http://10.0.2.2");
                        }
                        Log.d("ImageDebug", "Image URL: " + ivIdentityCardFaceUpUrl);
                        Glide.with(ConfirmDriverActivity.this)
                                .load(ivIdentityCardFaceUpUrl)
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logout)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(faceUpImageView);;
                    }
                    String ivIdentityCardFaceDownUrl = data.getIdentityCardFaceDownUrl();
                    if (ivIdentityCardFaceDownUrl != null && !ivIdentityCardFaceDownUrl.isEmpty()) {
                        if (ivIdentityCardFaceDownUrl.startsWith("http://localhost")) {
                            ivIdentityCardFaceDownUrl = ivIdentityCardFaceDownUrl.replace("http://localhost", "http://10.0.2.2");
                        }
                        Log.d("ImageDebug", "Image URL: " + ivIdentityCardFaceDownUrl);
                        Glide.with(ConfirmDriverActivity.this)
                                .load(ivIdentityCardFaceDownUrl)
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logout)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(faceDownImageView);;
                    }
                    String ivDriverLicenseUrl = data.getDriverLicenseUrl();
                    if (ivDriverLicenseUrl != null && !ivDriverLicenseUrl.isEmpty()) {
                        if (ivDriverLicenseUrl.startsWith("http://localhost")) {
                            ivDriverLicenseUrl = ivDriverLicenseUrl.replace("http://localhost", "http://10.0.2.2");
                        }
                        Log.d("ImageDebug", "Image URL: " + ivDriverLicenseUrl);
                        Glide.with(ConfirmDriverActivity.this)
                                .load(ivDriverLicenseUrl)
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logout)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(licenseImageView);;
                    }
                    vehicleTypeTextView.setText("Vehicle Type: " + data.getVehicleResponse().getVehicleType());
                    vehicleNumberTextView.setText("Vehicle Number:: " + data.getVehicleResponse().getVehicleNumber());
                    vehicleBrandTextView.setText("Vehicle Brand: " + data.getVehicleResponse().getVehicleBrand());
                    vehicleColorTextView.setText("Vehicle Color: " + data.getVehicleResponse().getVehicleColor());
                    seatsTextView.setText("Seats: " + data.getVehicleResponse().getNumberOfSeats());
                    statusTextView.setText("Vehicle Status: " + data.getVehicleResponse().getStatus());

                    String ivVehicleImageUrl = data.getVehicleResponse().getVehicleImageUrl();
                    if (ivVehicleImageUrl != null && !ivVehicleImageUrl.isEmpty()) {
                        if (ivVehicleImageUrl.startsWith("http://localhost")) {
                            ivVehicleImageUrl = ivVehicleImageUrl.replace("http://localhost", "http://10.0.2.2");
                        }
                        Log.d("ImageDebug", "Image URL: " + ivVehicleImageUrl);
                        Glide.with(ConfirmDriverActivity.this)
                                .load(ivVehicleImageUrl)
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logout)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(vehicleImageView);;
                    }
                    String ivCarregistrationUrl = data.getVehicleResponse().getCarregistrationUrl();
                    if (ivCarregistrationUrl != null && !ivCarregistrationUrl.isEmpty()) {
                        if (ivCarregistrationUrl.startsWith("http://localhost")) {
                            ivCarregistrationUrl = ivCarregistrationUrl.replace("http://localhost", "http://10.0.2.2");
                        }
                        Log.d("ImageDebug", "Image URL: " + ivCarregistrationUrl);
                        Glide.with(ConfirmDriverActivity.this)
                                .load(ivCarregistrationUrl)
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logout)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(carRegistrationImageView);;
                    }
                    String ivVehicleInspectionCertificateUrl = data.getVehicleResponse().getVehicleInspectionCertificateUrl();
                    if (ivVehicleInspectionCertificateUrl != null && !ivVehicleInspectionCertificateUrl.isEmpty()) {
                        if (ivVehicleInspectionCertificateUrl.startsWith("http://localhost")) {
                            ivVehicleInspectionCertificateUrl = ivVehicleInspectionCertificateUrl.replace("http://localhost", "http://10.0.2.2");
                        }
                        Log.d("ImageDebug", "Image URL: " + ivVehicleInspectionCertificateUrl);
                        Glide.with(ConfirmDriverActivity.this)
                                .load(ivVehicleInspectionCertificateUrl)
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logout)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(inspectionCertificateImageView);;
                    }

                    String ivCarInsuranceUrl = data.getVehicleResponse().getCarInsuranceUrl();
                    if (ivCarInsuranceUrl != null && !ivCarInsuranceUrl.isEmpty()) {
                        if (ivCarInsuranceUrl.startsWith("http://localhost")) {
                            ivCarInsuranceUrl = ivCarInsuranceUrl.replace("http://localhost", "http://10.0.2.2");
                        }
                        Log.d("ImageDebug", "Image URL: " + ivCarInsuranceUrl);
                        Glide.with(ConfirmDriverActivity.this)
                                .load(ivCarInsuranceUrl)
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logout)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(insuranceImageView);;
                    }
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
    private Retrofit getRetrofitClient() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);
        if (jwtToken == null) {
            Toast.makeText(ConfirmDriverActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
        }
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                .newBuilder()
                .addInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    if (jwtToken != null) {
                        requestBuilder.addHeader("Authorization", "Bearer " + jwtToken);
                    }
                    return chain.proceed(requestBuilder.build());
                })
                .build();


        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
