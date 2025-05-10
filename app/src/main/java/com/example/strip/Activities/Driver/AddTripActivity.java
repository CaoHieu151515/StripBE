package com.example.strip.Activities.Driver;


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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.strip.Activities.OpenStreetMapActivity;
import com.example.strip.Activities.Trip.EditTripActivity;
import com.example.strip.Models.DriverVehicleDTO;
import com.example.strip.Models.Request.TripCreateRequest;

import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.Models.TripDetail;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.DateFormatter;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;


import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private ImageView ivTripImage;
    private static final int REQUEST_IMAGE_PICK = 100;
    private byte[] ImageBytes;
    private int REQUEST_MAP = 1001;
    private String startLocation, endLocation;
    private double distance;
    private int duration;
    private String formatTimeShow, formatTimeStore, formatTimeStoreTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ivTripImage = findViewById(R.id.ivTripImage);
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
                startActivityForResult(intent, REQUEST_MAP);
            }
        });
        ivTripImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        retrofit = ApiClient.getClientWithToken(this);
        btnCreateTrip.setOnClickListener(v -> createTrip());
        etCondition.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn điều kiện");

            String[] conditions = {"Không hút thuốc", "Không mang thú cưng", "Không mang hành lý nặng quá 10kg"};
            boolean[] checkedItems = new boolean[conditions.length]; // Tất cả mặc định là false

            builder.setMultiChoiceItems(conditions, checkedItems, (dialog, which, isChecked) -> {
                checkedItems[which] = isChecked; // Cập nhật lựa chọn
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                StringBuilder selectedConditions = new StringBuilder();
                for (int i = 0; i < conditions.length; i++) {
                    if (checkedItems[i]) {
                        if (selectedConditions.length() > 0) selectedConditions.append(", ");
                        selectedConditions.append(conditions[i]);
                    }
                }
                etCondition.setText(selectedConditions.toString());
            });

            builder.setNegativeButton("Hủy", null);

            builder.show();
        });

        fetchUserInfo();

        etStartDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddTripActivity.this, (view, selectedYear, selectedMonth, selectedDay) -> {

                // After date is selected, show time picker
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddTripActivity.this, (timeView, selectedHour, selectedMinute) -> {

                    // Set calendar with both date and time
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(Calendar.YEAR, selectedYear);
                    selectedCalendar.set(Calendar.MONTH, selectedMonth);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    selectedCalendar.set(Calendar.MINUTE, selectedMinute);
                    selectedCalendar.set(Calendar.SECOND, 0);
                    selectedCalendar.set(Calendar.MILLISECOND, 0);

                    // Formatters
                    SimpleDateFormat sdfStore = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                    SimpleDateFormat sdfDisplay = new SimpleDateFormat("MM/dd/yyyy'\n'hh:mm:ss a", Locale.getDefault());

                    // Format start date
                    formatTimeStore = sdfStore.format(selectedCalendar.getTime());
                    formatTimeShow = sdfDisplay.format(selectedCalendar.getTime());
                    etStartDate.setText(formatTimeShow);

                    // Calculate etEndDate
                    Calendar calendarEndDate = (Calendar) selectedCalendar.clone();
                    calendarEndDate.add(Calendar.MINUTE, (int) duration);

                    String endDateDisplay = sdfDisplay.format(calendarEndDate.getTime());
                    etEndDate.setText(endDateDisplay);

                    // Format end date in ISO format
                    formatTimeStoreTwo = sdfStore.format(calendarEndDate.getTime());

                }, hour, minute, false // false = 12-hour format
                );

                timePickerDialog.show();
            }, year, month, day
            );

            datePickerDialog.show();
        });
    }
    private void createTrip() {
        String driverId = user.getDriver().getDriverID();
        String vehicleId = selectedVehicleId;
        int pricePerSeat = Integer.parseInt(etPricePerSeat.getText().toString().trim());
        int maxSeat = Integer.parseInt(etMaxSeat.getText().toString().trim());
        String startDate = formatTimeStore;
        String endDate = formatTimeStoreTwo;
        String startLocation = tvStartLocation.getText().toString().trim();
        String endLocation = tvEndLocation.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String condition = etCondition.getText().toString().trim();
        double totalDistance = Double.parseDouble(String.format("%.2f", distance));
        TripCreateRequest tripRequest = new TripCreateRequest(driverId, vehicleId, ImageBytes, "image/png",
                pricePerSeat, 0,maxSeat,startDate ,endDate, startLocation, endLocation, description, condition, totalDistance);

        ITripMobileApiService tripService = ApiClient.getClientWithToken(this).create(ITripMobileApiService.class);
        tripService.createTrip(tripRequest).enqueue(new Callback<TripDetail>() {
            @Override
            public void onResponse(Call<TripDetail> call, Response<TripDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TripDetail createdTrip = response.body();
                    Intent intent = new Intent(AddTripActivity.this, EditTripActivity.class);
                    intent.putExtra("tripId", createdTrip.getTripID()); // Pass tripId to detail activity
                    startActivity(intent);
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
                        vehicleIds.add(dto.getVehicleNumber()); // or use dto.getLicensePlate() or something else more readable
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
                            selectedVehicleId = vehicles.get(position).getVehicleId(); // Store selected vehicleId
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
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_PICK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                ivTripImage.setImageURI(selectedImageUri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    ImageBytes = stream.toByteArray();
                    Log.d("ImageBytes", "Byte array size: " + ImageBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to process image!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == REQUEST_MAP && resultCode == RESULT_OK && data != null) {
            startLocation = data.getStringExtra("startLocation");
            endLocation = data.getStringExtra("endLocation");
            distance = data.getDoubleExtra("distance", 0.0);
            duration = data.getIntExtra("duration", 0);
            tvStartLocation.setText("" + startLocation);
            tvEndLocation.setText("" + endLocation);
            tvDistance.setText(String.format("%.2f km", distance));
            tvDuration.setText(String.format("%d mins", duration));

            // Do something with the returned data
        }
    }
}
