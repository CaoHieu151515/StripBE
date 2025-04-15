package com.example.strip.Activities.Driver;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strip.Activities.OpenStreetMapActivity;
import com.example.strip.Models.DriverVehicleDTO;
import com.example.strip.Models.Request.TripCreateRequest;

import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.Models.TripDetail;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;


import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTripActivity extends AppCompatActivity{
    private EditText etPricePerSeat, etMaxSeat, etStartDate, etEndDate, etDescription, etCondition;
    private Button btnCreateTrip, btnShowRoute;
    private Retrofit retrofit;
    private TextView tvDistance, tvDuration, tvStartLocation, tvEndLocation;
    private UserMoreResponse user;
    private Spinner spinnerVehicle;
    private List<DriverVehicleDTO> vehicles; // from user.getDriverVehicleDTO()
    private String selectedVehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        spinnerVehicle = findViewById(R.id.spinnerVehicle);
        etPricePerSeat = findViewById(R.id.etPricePerSeat);
        etMaxSeat = findViewById(R.id.etMaxSeat);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etDescription = findViewById(R.id.etDescription);
        etCondition = findViewById(R.id.etCondition);
        btnCreateTrip = findViewById(R.id.btnCreateTrip);
        tvStartLocation = findViewById(R.id.tvStartLocation);
        tvEndLocation = findViewById(R.id.tvEndLocation);
        tvDistance = findViewById(R.id.tvDistanceValue);
        tvDuration = findViewById(R.id.tvDurationValue);
        btnShowRoute = findViewById(R.id.btnShowRoute);

        // Initialize MapView
        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTripActivity.this, ManageTripActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTripActivity.this, OpenStreetMapActivity.class);
                startActivity(intent);
            }
        });
        retrofit = ApiClient.getClientWithToken(this);
        btnCreateTrip.setOnClickListener(v -> createTrip());
        fetchUserInfo();

//        etStartDate.setOnClickListener(v -> {
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(
//                    this, (view, selectedYear, selectedMonth, selectedDay) -> {
//                // Create a Calendar object and set the selected date
//                Calendar selectedCalendar = Calendar.getInstance();
//                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
//                selectedCalendar.set(Calendar.MILLISECOND, 0); // Set milliseconds to 0
//
//                // Format the date to ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
//                String dob = sdf.format(selectedCalendar.getTime());
//
//                // Set the formatted date into the EditText
//                etStartDate.setText(dob);
//            }, year, month, day
//            );
//
//            datePickerDialog.show();
//        });
//        etEndDate.setOnClickListener(v -> {
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(
//                    this, (view, selectedYear, selectedMonth, selectedDay) -> {
//                // Create a Calendar object and set the selected date
//                Calendar selectedCalendar = Calendar.getInstance();
//                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
//                selectedCalendar.set(Calendar.MILLISECOND, 0); // Set milliseconds to 0
//
//                // Format the date to ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
//                String dob = sdf.format(selectedCalendar.getTime());
//
//                // etEndDate the formatted date into the EditText
//                etEndDate.setText(dob);
//            }, year, month, day
//            );
//
//            datePickerDialog.show();
//        });
    }
//    private Retrofit getRetrofitClient() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        String jwtToken = sharedPreferences.getString("jwtToken", null);
//        if (jwtToken == null) {
//            Toast.makeText(AddTripActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
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
    private void createTrip() {
        String driverId = "";
        String vehicleId = selectedVehicleId;
        int pricePerSeat = Integer.parseInt(etPricePerSeat.getText().toString().trim());
        int maxSeat = Integer.parseInt(etMaxSeat.getText().toString().trim());
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        String startLocation = "";
        String endLocation = "";
        String description = etDescription.getText().toString().trim();
        String condition = etCondition.getText().toString().trim();

        TripCreateRequest tripRequest = new TripCreateRequest(driverId, vehicleId, pricePerSeat, maxSeat,
                startDate, endDate, startLocation, endLocation, description, condition);

        ITripMobileApiService tripService = ApiClient.getClientWithToken(this).create(ITripMobileApiService.class);
        tripService.createTrip(tripRequest).enqueue(new Callback<TripDetail>() {
            @Override
            public void onResponse(Call<TripDetail> call, Response<TripDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AddTripActivity.this, "Trip Created Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Failed", "Failed to create trips!" + response.code());
                    Toast.makeText(AddTripActivity.this, "Failed to Create Trip!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TripDetail> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(AddTripActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchUserInfo() {
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService apiService = retrofit.create(IUserMobileApiService.class);
        Call<UserMoreResponse> call = apiService.getUserInfo();

        call.enqueue(new Callback<UserMoreResponse>() {
            @Override
            public void onResponse(Call<UserMoreResponse> call, Response<UserMoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
//                    tvDriverId.setText(user.getDriver().getDriverID() != null ? user.getDriver().getDriverID() : "N/A");
                    vehicles = user.getDriverVehicleDTO(); // List<DriverVehicleDTO>

// Create a list of vehicle IDs (or any display name)
                    List<String> vehicleIds = new ArrayList<>();
                    for (DriverVehicleDTO dto : vehicles) {
                        vehicleIds.add(dto.getVehicleId()); // or use dto.getLicensePlate() or something else more readable
                    }

// IMPORTANT: use AddTripActivity.this as context
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            AddTripActivity.this,
                            android.R.layout.simple_spinner_item,
                            vehicleIds
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerVehicle.setAdapter(adapter);

// Set selected vehicle ID when spinner is changed
                    spinnerVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedVehicleId = vehicles.get(position).getVehicleNumber(); // Store selected vehicleId
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            selectedVehicleId = null;
                        }
                    });

                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Error", "Lỗi khi lấy thông tin: " + errorBody);
                        Toast.makeText(AddTripActivity.this, "Lỗi khi lấy thông tin: " + response.code(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AddTripActivity.this, "Lỗi khi lấy thông tin: Không thể lấy thông báo lỗi" + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserMoreResponse> call, Throwable t) {
                Log.e("Error", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(AddTripActivity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
