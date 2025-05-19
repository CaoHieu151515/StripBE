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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Adapters.TripRequestAdapter;
import com.example.strip.Adapters.TripStopAdapter;
import com.example.strip.Models.Response.RequestTripResponse;
import com.example.strip.Models.StopLocation;
import com.example.strip.Models.TripDetail;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Utils.ErrorTranslate;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.Utils.TripStatusTranslate;
import com.example.strip.network.ApiClient;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripActiveDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerTripRequests;
    private TripRequestAdapter adapter;
    private List<RequestTripResponse> tripList = new ArrayList<>();
    private String tripId; // replace with actual ID from intent
    private Button btnStart, btnComplete;
    private TextView tvStartLocation, tvEndLocation,
            tvStartDate, tvEndDate, tvTripStatus;
    private ITripMobileApiService tripService;
    private ImageView ivDetail;
    private NotificationPopup notificationPopup;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_active_detail);
        recyclerTripRequests = findViewById(R.id.recyclerTripRequests);
        tvStartLocation = findViewById(R.id.tvStartLocation);
        tvEndLocation = findViewById(R.id.tvEndLocation);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvTripStatus = findViewById(R.id.tvTripStatus);
        btnStart = findViewById(R.id.btnStart);
        btnComplete = findViewById(R.id.btnComplete);
        ivDetail = findViewById(R.id.ivDetail);
        notificationPopup = new NotificationPopup(this);
        adapter = new TripRequestAdapter(this, tripList, new TripRequestAdapter.OnTripActionListener() {
            @Override
            public void onActionCompleted() {
                loadTripDetails(); // Reload the updated list
                fetchTripRequests(tripId);
            }
        });        recyclerTripRequests.setLayoutManager(new LinearLayoutManager(this));
        recyclerTripRequests.setAdapter(adapter);
        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(v -> finish());
        // get tripId from Intent if passed
        tripId = getIntent().getStringExtra("tripId");
        if (tripId == null) {
            Toast.makeText(this, "Invalid Trip ID!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ivDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TripActiveDetailActivity.this, EditTripActivity.class);
                intent.putExtra("tripId", tripId); // Pass tripId to detail activity
                startActivity(intent);
            }
        });
        loadTripDetails();
        fetchTripRequests(tripId);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ITripMobileApiService apiService = ApiClient.getClientWithToken(TripActiveDetailActivity.this).create(ITripMobileApiService.class);
                Call<Void> call = apiService.startTrip(tripId);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadTripDetails();
                        notificationPopup.showPopup("Trip started",false);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        notificationPopup.showPopup("Failed to start trip",true);
                    }
                });
            }
        });
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ITripMobileApiService apiService = ApiClient.getClientWithToken(TripActiveDetailActivity.this).create(ITripMobileApiService.class);
                Call<Void> call = apiService.completeTrip(tripId);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadTripDetails();
                        notificationPopup.showPopup("Trip completed",false);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        notificationPopup.showPopup("Failed to complete trip",true);
                    }
                });
            }
        });
    }
    private void fetchTripRequests(String tripId) {
        ITripMobileApiService service = ApiClient.getClientWithToken(this).create(ITripMobileApiService.class);
        Call<List<RequestTripResponse>> call = service.getAllRequestsByTripId(tripId);

        call.enqueue(new Callback<List<RequestTripResponse>>() {
            @Override
            public void onResponse(Call<List<RequestTripResponse>> call, Response<List<RequestTripResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setData(response.body());
                } else {
                    Log.e("Failed", "Failed to load trip requests!" + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Không tải được yêu cầu chuyến đi! " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Không tải được yêu cầu chuyến đi! \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Không tải được yêu cầu chuyến đi! \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RequestTripResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);            }
        });
    }
    private void loadTripDetails() {
        tripService = ApiClient.getClientWithToken(this).create(ITripMobileApiService.class);
        tripService.getTripDetails(tripId).enqueue(new Callback<TripDetail>() {
            @Override
            public void onResponse(@NonNull Call<TripDetail> call, @NonNull Response<TripDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TripDetail trip = response.body();
                    tvStartLocation.setText(trip.getStartLocation());
                    tvEndLocation.setText(trip.getEndLocation());
                    tvTripStatus.setText(TripStatusTranslate.translateStatus(trip.getTripStatus()));
                    String originalDateString = trip.getStartDate(); // Example: "2025-04-16T13:45:00" (ISO format)
                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault());
                    try {
                        Date date = originalFormat.parse(originalDateString);
                        String formattedDate = displayFormat.format(date);
                        tvStartDate.setText(formattedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        tvStartDate.setText("Invalid date");
                    }
                    String originalDateString2 = trip.getEndDate(); // Example: "2025-04-16T13:45:00" (ISO format)
                    SimpleDateFormat originalFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat displayFormat2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault());
                    try {
                        Date date = originalFormat2.parse(originalDateString2);
                        String formattedDate2 = displayFormat2.format(date);
                        tvEndDate.setText(formattedDate2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        tvEndDate.setText("Invalid date");
                    }
                } else {
                    Log.e("Failed", "Failed to load trips detail!" + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Không tải được chi tiết chuyến đi! " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Không tải được chi tiết chuyến đi! \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Không tải được chi tiết chuyến đi! \n Không thể lấy thông báo lỗi", true);
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
}
