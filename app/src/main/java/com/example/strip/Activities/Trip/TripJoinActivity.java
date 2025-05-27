package com.example.strip.Activities.Trip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.strip.Activities.Driver.AddTripActivity;
import com.example.strip.Adapters.TripStopTwoAdapter;
import com.example.strip.Models.DriverVehicleDTO;
import com.example.strip.Models.Request.JoinTripRequest;
import com.example.strip.Models.Request.NotificationRequest;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.Models.StopLocation;
import com.example.strip.Models.TripDetail;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.ErrorTranslate;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.Utils.TripStatusTranslate;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TripJoinActivity extends AppCompatActivity {
    private ITripMobileApiService tripService;
    private TextView tvStartLocation, tvEndLocation,tvStartLocaId, tvEndLocaId;
    private String tripId;
    private RecyclerView recyclerTripStops;

    EditText etNumberOfSeats, etLuggageDescription;
    Button btnJoinTrip;
    private Button btnPickStartLoca, btnPickEndLoca;
    private String selectedStartLocaId = "", selectedEndLocaId = "";
    private String lastClicked = ""; // "start" or "end"
    private NotificationPopup notificationPopup;
    private UserMoreResponse user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_join); // Update with your actual XML file name
        tvStartLocation = findViewById(R.id.tvStartLocation);
        tvEndLocation = findViewById(R.id.tvEndLocation);
        recyclerTripStops = findViewById(R.id.recyclerTripStops);
        etNumberOfSeats = findViewById(R.id.etNumberOfSeats);
        etLuggageDescription = findViewById(R.id.etLuggageDescription);
        tvStartLocaId = findViewById(R.id.tvStartLocaId);
        tvEndLocaId = findViewById(R.id.tvEndLocaId);
        btnPickStartLoca = findViewById(R.id.btnStartLocation);
        btnPickEndLoca = findViewById(R.id.btnEndLocation);
        notificationPopup = new NotificationPopup(this);
        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPickStartLoca.setOnClickListener(v -> {
            lastClicked = "start";
            Toast.makeText(this, "Select a stop for Start Location", Toast.LENGTH_SHORT).show();
        });

        btnPickEndLoca.setOnClickListener(v -> {
            lastClicked = "end";
            Toast.makeText(this, "Select a stop for End Location", Toast.LENGTH_SHORT).show();
        });
        btnJoinTrip = findViewById(R.id.btnJoinTrip);

        LinearLayoutManager layoutManager = new LinearLayoutManager(TripJoinActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerTripStops.setLayoutManager(layoutManager);

        tripId = getIntent().getStringExtra("tripId");
        if (tripId == null) {
            Toast.makeText(this, "Invalid Trip ID!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        loadTripDetails();
        fetchUserInfo();
        btnJoinTrip.setOnClickListener(v -> sendJoinRequest());
    }
//    private Retrofit getRetrofitClient() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        String jwtToken = sharedPreferences.getString("jwtToken", null);
//        if (jwtToken == null) {
//            Toast.makeText(TripJoinActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
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
                    List<StopLocation> stops = trip.getStopLocations();
                    if (stops != null && !stops.isEmpty()) {
                        // Sort stops by tripPosition
                        Collections.sort(stops, Comparator.comparingInt(StopLocation::getTripPositon));

                        TripStopTwoAdapter adapter = new TripStopTwoAdapter(TripJoinActivity.this, stops, selectedStop -> {
                            if (lastClicked.equals("start")) {
                                tvStartLocaId.setText(selectedStop.getTripPositon() + ". " + selectedStop.getStopLoca());
                                selectedStartLocaId = selectedStop.getStopLocaID();
                            } else if (lastClicked.equals("end")) {
                                tvEndLocaId.setText(selectedStop.getTripPositon() + ". " + selectedStop.getStopLoca());
                                selectedEndLocaId = selectedStop.getStopLocaID();
                            } else {
                                notificationPopup.showPopup("Làm ơn chọn điểm đi và điểm đến", true);
                            }
                        });
                        recyclerTripStops.setAdapter(adapter);
                    }
                } else {
                    Log.e("Failed", "Failed to load trips!" + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Không tải được chuyến đi! " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Không tải được chuyến đi! \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Không tải được chuyến đi! \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TripDetail> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }
    private void sendJoinRequest() {
        String userId = user.getDriver().getUserId();
        int seats = Integer.parseInt(etNumberOfSeats.getText().toString());
        String luggage = etLuggageDescription.getText().toString();
        String now = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .format(new Date());
        JoinTripRequest request = new JoinTripRequest();
        request.setTripId(tripId);
        request.setNumberOfSeats(seats);
        request.setLuggageDescription(luggage);
        request.setType("LUGGAGE");
        request.setPickUpTime(now);
        request.setAmountApproveFee(0); // Example fee
        request.setStartLoca(selectedStartLocaId);
        request.setEndLoca(selectedEndLocaId);
        request.setPayNow(false);

        tripService.joinTrip(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    NotificationRequest notiRequest = new NotificationRequest(
                            "Bạn đã tham gia chuyến đi thành công!", // title or message
                            "Mã chuyến đi: " + tripId, // detailed message
                            userId // or other target
                    );
                    notificationPopup.createNotification(notiRequest);
                    notificationPopup.showPopup("Tham gia chuyến đi thành công", false);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        finish();
                    }, 1500);
                } else {
                    Log.e("Failed", "Thất bại khi tham gia chuyến đi!" + response.code());

                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Không thể tham gia chuyến đi. " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);

                        String errorMessageCode = "";

                        // Trường hợp 1: properties là JSONObject
                        if (jsonObject.has("properties")) {
                            Object props = jsonObject.get("properties");

                            if (props instanceof JSONObject) {
                                errorMessageCode = ((JSONObject) props).optString("message", "");
                            } else if (props instanceof String) {
                                // Trường hợp 2: properties là chuỗi như {message=error.already-exists, params=feedback}
                                String propsStr = (String) props;
                                // Chuyển sang dạng JSON hợp lệ
                                propsStr = propsStr.replace("=", "\":\"").replace(", ", "\", \"").replace("{", "{\"").replace("}", "\"}");

                                try {
                                    JSONObject propsJson = new JSONObject(propsStr);
                                    errorMessageCode = propsJson.optString("message", "");
                                } catch (Exception e) {
                                    errorMessageCode = "Lỗi không xác định";
                                }
                            }
                        }
                        // Fallback nếu không có message
                        if (errorMessageCode.isEmpty()) {
                            errorMessageCode = jsonObject.optString("message", "Lỗi không xác định");
                        }

                        // Dịch lỗi
                        String translated = ErrorTranslate.translateError(errorMessageCode);
                        notificationPopup.showPopup("Không thể tham gia chuyến đi.\n" + translated, true);

                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Không thể tham gia chuyến đi.\nKhông thể lấy thông báo lỗi", true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);            }
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
                } else {
                    try {
                        String errorBody = response.errorBody().string();

                        Log.e("Error", "Lỗi khi lấy thông tin: " + errorBody);
                        Toast.makeText(TripJoinActivity.this, "Lỗi khi lấy thông tin: " + response.code(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Lỗi khi lấy thông tin: \nKhông thể lấy thông báo lỗi\n" + response.code(), true);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserMoreResponse> call, Throwable t) {
                Log.e("Error", "Lỗi khi gọi API: " + t.getMessage(), t);
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }
}
