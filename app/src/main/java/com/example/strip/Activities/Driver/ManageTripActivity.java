package com.example.strip.Activities.Driver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Adapters.TripActiveAdapter;
import com.example.strip.Adapters.TripAdapter;
import com.example.strip.Models.Response.TripActiveResponse;
import com.example.strip.Models.Trip;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageTripActivity extends AppCompatActivity {
    private RecyclerView recyclerViewTrips;
    private RecyclerView recyclerView;
    private TripActiveAdapter tripActiveAdapter;
    private List<TripActiveResponse> tripList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trip);
        ImageView btnBack = findViewById(R.id.backButton);
        ImageView btnAdd = findViewById(R.id.addTripButton);
        recyclerView = findViewById(R.id.recyclerView);
        tripActiveAdapter = new TripActiveAdapter(ManageTripActivity.this, tripList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tripActiveAdapter);
        fetchTrips();
        // Find the ImageView by its ID
        // Set an OnClickListener for the ImageView
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                Intent intent = new Intent(ManageTripActivity.this, AddTripActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() if you want to close the IntroductionActivity
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        recyclerViewTrips = findViewById(R.id.recyclerViewTrips);
//
//        // Set up RecyclerView with horizontal scrolling
//        LinearLayoutManager layoutManager = new LinearLayoutManager(ManageTripActivity.this, LinearLayoutManager.VERTICAL, false);
//        recyclerViewTrips.setLayoutManager(layoutManager);
//        loadTrips();
    }
//    private Retrofit getRetrofitClient() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        String jwtToken = sharedPreferences.getString("jwtToken", null);
//        if (jwtToken == null) {
//            Toast.makeText(ManageTripActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
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
//    private void loadTrips() {
//        ITripMobileApiService tripService = ApiClient.getClientWithToken(this).create(ITripMobileApiService.class);
//        tripService.getAllTrips().enqueue(new Callback<List<Trip>>() {
//            @Override
//            public void onResponse(@NonNull Call<List<Trip>> call, @NonNull Response<List<Trip>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    tripAdapter = new TripTwoAdapter(ManageTripActivity.this, response.body());
//                    recyclerViewTrips.setAdapter(tripAdapter);
//                } else {
//                    Log.e("Failed", "Failed to load trips!" + response.code());
//                    Toast.makeText(ManageTripActivity.this, "Failed to load trips!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<Trip>> call, @NonNull Throwable t) {
//                Toast.makeText(ManageTripActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    private void fetchTrips() {
        ITripMobileApiService service = ApiClient.getClientWithToken(this).create(ITripMobileApiService.class);
        Call<List<TripActiveResponse>> call = service.getActiveTrips(0, 20);

        call.enqueue(new Callback<List<TripActiveResponse>>() {
            @Override
            public void onResponse(Call<List<TripActiveResponse>> call, Response<List<TripActiveResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tripList.clear();
                    tripList.addAll(response.body());
                    tripActiveAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<TripActiveResponse>> call, Throwable t) {
                Toast.makeText(ManageTripActivity.this, "Failed to load trips", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
