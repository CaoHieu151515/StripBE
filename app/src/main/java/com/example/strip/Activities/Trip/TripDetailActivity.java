package com.example.strip.Activities.Trip;

import android.content.Intent;
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
import com.example.strip.Models.TripDetail;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Utils.DateFormatter;
import com.example.strip.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDetailActivity extends AppCompatActivity {
    private TextView tvStartLocation, tvEndLocation, tvPrice, tvDescription, tvPricePerSeat, tvSeats,
            tvDistance,
            txtTime1, txtTitle1, txtTime2, txtTitle2,
            txtRating, txtRatingPlace, tvDriverName,
            tvVehicleType, tvVehicleColor, tvVehicleBrand;
    private ImageView ivTripImage, ivDriverImage, ivVehicleImage;
    private ITripMobileApiService tripService;
    private Button btnBook;
    private String tripId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail); // Update with your actual XML file name
        ImageView btnBack = findViewById(R.id.imgBack);
        tvVehicleType = findViewById(R.id.tvVehicleType);
        tvVehicleColor = findViewById(R.id.tvVehicleColor);
        tvVehicleBrand = findViewById(R.id.tvVehicleBrand);
        // Initialize UI components
        tvStartLocation = findViewById(R.id.tvStartLocation);
        tvEndLocation = findViewById(R.id.tvEndLocation);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvPricePerSeat = findViewById(R.id.tvPricePerSeat);
        tvSeats = findViewById(R.id.tvSeats);
        tvDistance = findViewById(R.id.tvDistance);
        ivTripImage = findViewById(R.id.ivTripImage);
        ivDriverImage = findViewById(R.id.ivDriverImage);
        btnBook = findViewById(R.id.btnBook);
        txtTime1 = findViewById(R.id.txtTime1);
        txtTitle1 = findViewById(R.id.txtTitle1);
        txtTime2 = findViewById(R.id.txtTime2);
        txtTitle2 = findViewById(R.id.txtTitle2);
        txtRating = findViewById(R.id.txtRating);
        txtRatingPlace = findViewById(R.id.txtRatingPlace);
        tvDriverName = findViewById(R.id.tvDriverName);
        ivVehicleImage = findViewById(R.id.ivVehicleImage);
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
                    tvStartLocation.setText(trip.getStartLocation() + "");
                    tvEndLocation.setText(trip.getEndLocation() + "");
                    tvPrice.setText("" + trip.getPricePerSeat());
                    tvDescription.setText("" + trip.getDescription());
                    tvPricePerSeat.setText("" + trip.getPricePerSeat() + " VND");
                    tvSeats.setText("" + trip.getCurrentSeat() + "/" + trip.getMaxSeat() + " seats");
                    tvDistance.setText(trip.getTotalDistance() + " km");
                    tvDriverName.setText(trip.getDriverOfTripDetail().getLastName() + "");
                    txtRating.setText(trip.getDriverOfTripDetail().getRating() + "★");
                    txtRatingPlace.setText(trip.getVehicleOfTripDetail().getVehicleNumber() + "");

                    txtTime1.setText(DateFormatter.formatDate(trip.getStartDate()) + "");
                    txtTime2.setText(DateFormatter.formatDate(trip.getEndDate()) + "");

                    txtTitle1.setText(trip.getStartLocation() + "");
                    txtTitle2.setText(trip.getEndLocation() + "");
                    tvVehicleType.setText(trip.getVehicleOfTripDetail().getVehicleType() + "");
                    tvVehicleBrand.setText(trip.getVehicleOfTripDetail().getVehicleBrand() + "");
                    tvVehicleColor.setText(trip.getVehicleOfTripDetail().getVehicleColor() + "");

                    String imageUrlVehicle = trip.getTripImgUrl();
                    if (imageUrlVehicle != null && !imageUrlVehicle.isEmpty()) {
                        if (imageUrlVehicle.startsWith("https://localhost")) {
                            imageUrlVehicle = imageUrlVehicle.replace("https://localhost", "http://10.0.2.2");
                        }
                        if (!TripDetailActivity.this.isFinishing()
                                && !TripDetailActivity.this.isDestroyed()) {
                            Glide.with(TripDetailActivity.this).load(imageUrlVehicle).into(ivVehicleImage);
                        }
                    }


                    // Load trip image
                    String imageUrlTrip = trip.getTripImgUrl();
                    if (imageUrlTrip != null && !imageUrlTrip.isEmpty()) {
                        if (imageUrlTrip.startsWith("https://localhost")) {
                            imageUrlTrip = imageUrlTrip.replace("https://localhost", "http://10.0.2.2");
                        }
                        if (!TripDetailActivity.this.isFinishing()
                                && !TripDetailActivity.this.isDestroyed()) {
                            Glide.with(TripDetailActivity.this).load(imageUrlTrip).into(ivTripImage);
                        }
                    }

                    String imageUrlDriver = trip.getDriverOfTripDetail().getAvatarUrl();
                    if (imageUrlDriver != null && !imageUrlDriver.isEmpty()) {
                        if (imageUrlDriver.startsWith("https://localhost")) {
                            imageUrlDriver = imageUrlDriver.replace("https://localhost", "http://10.0.2.2");
                        }
                        if (!TripDetailActivity.this.isFinishing()
                                && !TripDetailActivity.this.isDestroyed()) {
                            Glide.with(TripDetailActivity.this).load(imageUrlDriver).into(ivDriverImage);
                        }
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
