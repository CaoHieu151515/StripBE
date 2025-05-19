package com.example.strip.Activities.Trip;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.strip.Activities.Driver.AddTripActivity;
import com.example.strip.Activities.OpenStreetMapActivity;
import com.example.strip.Adapters.TripStopAdapter;
import com.example.strip.Models.DriverVehicleDTO;
import com.example.strip.Models.LocationInfo;
import com.example.strip.Models.Request.StopLocationUpdateRequest;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.Models.StopLocation;
import com.example.strip.Models.TripDetail;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditTripActivity extends AppCompatActivity {
    private ITripMobileApiService tripService;
    private TextView tvStartLocation, tvEndLocation,
            tvStartDate, tvEndDate,
            tvStopLoca, tvEstimatedTime, tvEstimatedKM, tvStopLocaPosition;
    private Button btnFindLocation, btnAddLocation, btnUpdateTripLocation;
    private String tripId;
    private RecyclerView recyclerTripStops;
    private String lastClicked = "";
    private String selectedStartLocaId = "", selectedEndLocaId = "";
    private List<StopLocationUpdateRequest> tempStopList = new ArrayList<>();
    private TripStopAdapter tripStopAdapter;
    private int REQUEST_MAP = 1001;
    private String startLocation, endLocation, formatTimeShow, formatTimeStore;

    private double distance;
    private int position = 1, duration;
    private List<LocationInfo> availableLocations = new ArrayList<>();
    private EditText edtStopLocaTime;
    private NotificationPopup notificationPopup;
    // Inside EditTripActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        tvStartLocation = findViewById(R.id.tvStartLocation);
        tvEndLocation = findViewById(R.id.tvEndLocation);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvStopLoca = findViewById(R.id.tvStopLoca);
        tvEstimatedTime = findViewById(R.id.tvEstimatedTime);
        tvEstimatedKM = findViewById(R.id.tvEstimatedKM);
        tvStopLocaPosition = findViewById(R.id.tvStopLocaPosition);
        btnFindLocation = findViewById(R.id.btnFindLocation);
        btnAddLocation = findViewById(R.id.btnAddLocation);
        btnUpdateTripLocation = findViewById(R.id.btnUpdateTripLocation);
        recyclerTripStops = findViewById(R.id.recyclerTripStops);
        availableLocations = loadLocationInfos(); // Your method to load saved locations
        edtStopLocaTime = findViewById(R.id.edtStopLocaTime);
        notificationPopup = new NotificationPopup(this);
        tvStopLoca.setOnClickListener(v -> {
            if (availableLocations.isEmpty()) return;

            String selectedStartLocation = tvStartLocation.getText().toString();

            // Filter locations by matching start location
            List<LocationInfo> filteredLocations = new ArrayList<>();
            for (LocationInfo loc : availableLocations) {
                if (loc.getStartLocation().equalsIgnoreCase(selectedStartLocation)) {
                    filteredLocations.add(loc);
                }
            }

            if (filteredLocations.isEmpty()) {
                notificationPopup.showPopup("Không có điểm dừng phù hợp với điểm đi đã chọn.\n Làm ơn thêm điểm dừng!",true);
                return;
            }

            // Sort by distance (ascending)
            Collections.sort(filteredLocations, Comparator.comparingDouble(loc -> parseDistanceToDouble(loc.getDistance())));

            String[] locationNames = new String[filteredLocations.size()];
            for (int i = 0; i < filteredLocations.size(); i++) {
                locationNames[i] =
                        "Từ: " + filteredLocations.get(i).getStartLocation() + "\n" +
                        "Đến: " + filteredLocations.get(i).getEndLocation() + "\n" +
                        "Khoảng thời gian: " + filteredLocations.get(i).getDuration() + "\n" +
                        "Khoảng cách: " + filteredLocations.get(i).getDistance() + "\n";
            }

            new AlertDialog.Builder(EditTripActivity.this)
                    .setTitle("Lựa chọn điểm dừng chân")
                    .setItems(locationNames, (dialog, which) -> {
                        LocationInfo selected = filteredLocations.get(which);
                        tvStopLoca.setText(selected.getEndLocation());
                        tvEstimatedTime.setText(selected.getDuration());
                        tvEstimatedKM.setText(selected.getDistance());
                        duration = parseDurationToInt(selected.getDuration());
                        distance = parseDistanceToDouble(selected.getDistance());
                        calculateAndSetStopLocaTime(edtStopLocaTime);
                    })
                    .show();
        });





        tvStopLocaPosition.setText("" + position);

        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(v -> finish());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerTripStops.setLayoutManager(layoutManager);

        tripStopAdapter = new TripStopAdapter(
                EditTripActivity.this,
                new ArrayList<>(), // initially empty
                selectedStop -> {
                    // Handle selection logic
                }
        );
        edtStopLocaTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditTripActivity.this, (view, selectedYear, selectedMonth, selectedDay) -> {

                // After date is selected, show time picker
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EditTripActivity.this, (timeView, selectedHour, selectedMinute) -> {

                    // Set calendar with both date and time
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(Calendar.YEAR, selectedYear);
                    selectedCalendar.set(Calendar.MONTH, selectedMonth);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    selectedCalendar.set(Calendar.MINUTE, selectedMinute);
                    selectedCalendar.set(Calendar.SECOND, 0);
                    selectedCalendar.set(Calendar.MILLISECOND, 0);
                    SimpleDateFormat sdfStore = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                    formatTimeStore = sdfStore.format(selectedCalendar.getTime());
                    // Format with line break between date and time
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy'\n'hh:mm:ss a", Locale.getDefault());
                    formatTimeShow = sdf.format(selectedCalendar.getTime());

                    edtStopLocaTime.setText(formatTimeShow);

                }, hour, minute, false // false = 12-hour format
                );

                timePickerDialog.show();
            }, year, month, day
            );

            datePickerDialog.show();
        });


        btnFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditTripActivity.this, TripStoreActivity.class);
                intent.putExtra("startLocation", tvStartLocation.getText().toString());
                startActivityForResult(intent, REQUEST_MAP);
            }
        });
        recyclerTripStops.setAdapter(tripStopAdapter);

        tripId = getIntent().getStringExtra("tripId");
        if (tripId == null) {
            Toast.makeText(this, "Invalid Trip ID!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        loadTripDetails();

        btnUpdateTripLocation.setOnClickListener(v -> {
            if (tempStopList.isEmpty()) {
                notificationPopup.showPopup("Chưa có điểm dừng nào để cập nhật!",true);
                return;
            }
            updateTripStops();
        });
//
        btnAddLocation.setOnClickListener(v -> {
            try {
                StopLocationUpdateRequest newStop = new StopLocationUpdateRequest();
                newStop.setStopLoca(tvStopLoca.getText().toString());
                newStop.setStopLocaTime(formatTimeStore);
                newStop.setStopLocaStatus("UPCOMING");
                newStop.setEstimatedTime(duration);
                newStop.setEstimatedKM(distance);
                newStop.setStoplocaPosition(position);

                tempStopList.add(newStop); // for updateTripStops()

                // Convert to StopLocation for display
                StopLocation displayStop = new StopLocation();
                displayStop.setStopLoca(newStop.getStopLoca());
                displayStop.setStopLocaTime(newStop.getStopLocaTime());
                displayStop.setStopLocaStatus(newStop.getStopLocaStatus());
                displayStop.setEstimatedTime(newStop.getEstimatedTime());
                displayStop.setEstimatedKM(newStop.getEstimatedKM());
                displayStop.setTripPositon(newStop.getStoplocaPosition());

                // Get current adapter list
                List<StopLocation> currentList = ((TripStopAdapter) recyclerTripStops.getAdapter()).getStopList();
                currentList.add(displayStop);
                tripStopAdapter.notifyItemInserted(currentList.size() - 1);
                loadTripDetails();

                tvStopLoca.setText("");
                tvEstimatedTime.setText("");
                tvEstimatedKM.setText("");
                edtStopLocaTime.setText("");
                position++;
                tvStopLocaPosition.setText("" + position);

            } catch (Exception ex) {
                notificationPopup.showPopup("Thông tin điểm dừng không hợp lệ!" + ex.getMessage(),true);
            }
        });

    }
    private int parseDurationToInt(String durationStr) {
        // Example: "45 mins" or "45 minutes"
        return Integer.parseInt(durationStr.replaceAll("[^\\d]", ""));
    }

    private double parseDistanceToDouble(String distanceStr) {
        // Example: "12.5 km"
        return Double.parseDouble(distanceStr.replaceAll("[^\\d.]", ""));
    }
    private void calculateAndSetStopLocaTime(EditText edtStopLocaTime) {
        try {
            // Get the date string from tvStartDate (e.g., "04/16/2025 01:45:00 PM")
            String startDateStr = tvStartDate.getText().toString();
            String durationStr = tvEstimatedTime.getText().toString(); // e.g., "45 mins"

            // Match the format used when displaying tvStartDate
            SimpleDateFormat sdfInput = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault());
            Date startDate = sdfInput.parse(startDateStr);

            // Parse duration in minutes (remove any non-numeric characters)
            int durationMins = Integer.parseInt(durationStr.replaceAll("[^\\d]", ""));

            // Add the duration to the start date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.MINUTE, durationMins);

            // Format for display and storage
            SimpleDateFormat sdfStore = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            formatTimeStore = sdfStore.format(calendar.getTime());

            SimpleDateFormat sdfShow = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault());
            formatTimeShow = sdfShow.format(calendar.getTime());

            // Set the result
            edtStopLocaTime.setText(formatTimeShow);

        } catch (Exception e) {
            Toast.makeText(this, "Invalid time format: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private List<LocationInfo> loadLocationInfos() {
        SharedPreferences prefs = getSharedPreferences("LOCATION_PREFS", MODE_PRIVATE);
        String json = prefs.getString("location_list", "[]");
        Type type = new TypeToken<ArrayList<LocationInfo>>(){}.getType();
        return new Gson().fromJson(json, type);
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
                    List<StopLocation> stops = trip.getStopLocations();
                    if (stops != null && !stops.isEmpty()) {
                        // Sort stops by tripPosition in ascending order
                        stops.sort(Comparator.comparingInt(StopLocation::getTripPositon));

                        TripStopAdapter adapter = new TripStopAdapter(EditTripActivity.this, stops, selectedStop -> {
                            if (lastClicked.equals("start")) {
                                selectedStartLocaId = selectedStop.getStopLocaID();
                            } else if (lastClicked.equals("end")) {
                                selectedEndLocaId = selectedStop.getStopLocaID();
                            } else {
                                Toast.makeText(EditTripActivity.this, "Please select Start or End button first", Toast.LENGTH_SHORT).show();
                            }
                        });

                        recyclerTripStops.setAdapter(adapter);
                        tvStopLocaPosition.setText(String.valueOf(position));
                    }

                } else {
                    Log.e("Failed", "Failed to load trips!" + response.code());
                    Toast.makeText(EditTripActivity.this, "Failed to load trip details!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TripDetail> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(EditTripActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateTripStops() {
        tripService.updateTripLocations(tripId, tempStopList).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditTripActivity.this, "Stops updated successfully!", Toast.LENGTH_SHORT).show();
                    loadTripDetails();
                } else {
                    Toast.makeText(EditTripActivity.this, "Failed to update: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(EditTripActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MAP && resultCode == RESULT_OK && data != null) {
            startLocation = data.getStringExtra("startLocation");
            endLocation = data.getStringExtra("endLocation");
            distance = data.getDoubleExtra("distance", 0.0);
            duration = data.getIntExtra("duration", 0);

            tvStopLoca.setText("" + endLocation);
            tvEstimatedKM.setText(String.format("%.2f km", distance));
            tvEstimatedTime.setText(String.format("%d mins", duration));

            // Do something with the returned data
        }
    }

}
