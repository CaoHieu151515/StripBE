package com.example.strip.Activities.Trip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.strip.Activities.Driver.WalletActivity;
import com.example.strip.Activities.StripActivity;
import com.example.strip.Models.TripDetail;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TripDetailActivity extends AppCompatActivity {
    private TextView tvStartLocation, tvEndLocation, tvPrice, tvDescription, tvPricePerSeat, tvSeats,tvVehicleNumber;
    private ImageView ivTripImage, ivVehicleImage;
    private ITripMobileApiService tripService;
    private Button btnBook;
    private String tripId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail); // Update with your actual XML file name
        ImageView btnBack = findViewById(R.id.imgBack);
        // Initialize UI components
        tvStartLocation = findViewById(R.id.tvStartLocation);
        tvEndLocation = findViewById(R.id.tvEndLocation);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvPricePerSeat = findViewById(R.id.tvPricePerSeat);
        tvSeats = findViewById(R.id.tvSeats);
        tvVehicleNumber = findViewById(R.id.tvVehicleNumber);
        ivTripImage = findViewById(R.id.ivTripImage);
        ivVehicleImage = findViewById(R.id.ivVehicleImage);
        btnBook = findViewById(R.id.btnBook);


        // Get tripId from intent
        tripId = getIntent().getStringExtra("tripId");
        if (tripId == null) {
            Toast.makeText(this, "Invalid Trip ID!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch trip details
        loadTripDetails();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TripDetailActivity.this, TripJoinActivity.class);
                intent.putExtra("tripId", tripId); // Pass tripId to detail activity
                startActivity(intent);
                finish();
            }
        });
    }
//    private Retrofit getRetrofitClient() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        String jwtToken = sharedPreferences.getString("jwtToken", null);
//        if (jwtToken == null) {
//            Toast.makeText(TripDetailActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
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
//        return new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/")
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }
    private void loadTripDetails() {
        tripService = ApiClient.getClientWithToken(this).create(ITripMobileApiService.class);
        tripService.getTripDetails(tripId).enqueue(new Callback<TripDetail>() {
            @Override
            public void onResponse(@NonNull Call<TripDetail> call, @NonNull Response<TripDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TripDetail trip = response.body();
                    tvStartLocation.setText(trip.getStartLocation());
                    tvEndLocation.setText(trip.getEndLocation());
                    tvPrice.setText("" + trip.getPricePerSeat());
                    tvDescription.setText("" + trip.getDescription());
                    tvPricePerSeat.setText("" + trip.getPricePerSeat());
                    tvVehicleNumber.setText("" + trip.getVehicleNumber());
                    tvSeats.setText("" + trip.getCurrentSeat() + "/" + trip.getMaxSeat());

                    // Load trip image
                    String imageUrlTrip = trip.getTripImgUrl();
                    if (imageUrlTrip != null && !imageUrlTrip.isEmpty()) {
                        if (imageUrlTrip.startsWith("https://localhost")) {
                            imageUrlTrip = imageUrlTrip.replace("https://localhost", "http://10.0.2.2");
                        }
                        Glide.with(TripDetailActivity.this).load(imageUrlTrip).into(ivTripImage);
                    }
                    String imageUrlVehicle = trip.getVehicleImageUrl();
                    if (imageUrlVehicle != null && !imageUrlVehicle.isEmpty()) {
                        if (imageUrlVehicle.startsWith("https://localhost")) {
                            imageUrlVehicle = imageUrlVehicle.replace("https://localhost", "http://10.0.2.2");
                        }
                        Glide.with(TripDetailActivity.this).load(imageUrlVehicle).into(ivVehicleImage);
                    }
                } else {
                    Log.e("Failed", "Failed to load trips!" + response.code());
                    Toast.makeText(TripDetailActivity.this, "Failed to load trip details!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TripDetail> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(TripDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
