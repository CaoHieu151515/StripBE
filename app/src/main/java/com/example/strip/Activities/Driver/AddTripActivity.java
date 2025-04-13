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
    private EditText etPricePerSeat, etMaxSeat, etStartDate, etEndDate,
            etStartLocation, etEndLocation, etDescription, etCondition;
    private Button btnCreateTrip, btnShowRoute;
    private Retrofit retrofit;
    private TextView tvDistance, tvDuration, tvDriverId;
    private MapView mapView;
    private UserMoreResponse user;
    private Spinner spinnerVehicle;
    private List<DriverVehicleDTO> vehicles; // from user.getDriverVehicleDTO()
    private String selectedVehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        tvDriverId = findViewById(R.id.tvDriverId);
        spinnerVehicle = findViewById(R.id.spinnerVehicle);
        etPricePerSeat = findViewById(R.id.etPricePerSeat);
        etMaxSeat = findViewById(R.id.etMaxSeat);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etStartLocation = findViewById(R.id.etStartLocation);
        etEndLocation = findViewById(R.id.etEndLocation);
        etDescription = findViewById(R.id.etDescription);
        etCondition = findViewById(R.id.etCondition);
        btnCreateTrip = findViewById(R.id.btnCreateTrip);
        etStartLocation = findViewById(R.id.etStartLocation);
        etEndLocation = findViewById(R.id.etEndLocation);
        tvDistance = findViewById(R.id.tvDistance);
        tvDuration = findViewById(R.id.tvDuration);
        btnShowRoute = findViewById(R.id.btnShowRoute);
        Configuration.getInstance().setUserAgentValue("MyAppName/1.0 (Android)");

        // Initialize MapView
        mapView = findViewById(R.id.mapview);
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Set initial location of the map (e.g., Berlin)
        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(new GeoPoint(10.7769, 106.7009));
        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTripActivity.this, ManageTripActivity.class);
                startActivity(intent);
                finish();
            }
        });

        retrofit = ApiClient.getClientWithToken(this);

        btnCreateTrip.setOnClickListener(v -> createTrip());
        fetchUserInfo();

        btnShowRoute.setOnClickListener(v -> showRouteOnMap());
        etStartDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, (view, selectedYear, selectedMonth, selectedDay) -> {
                // Create a Calendar object and set the selected date
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
                selectedCalendar.set(Calendar.MILLISECOND, 0); // Set milliseconds to 0

                // Format the date to ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                String dob = sdf.format(selectedCalendar.getTime());

                // Set the formatted date into the EditText
                etStartDate.setText(dob);
            }, year, month, day
            );

            datePickerDialog.show();
        });
        etEndDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, (view, selectedYear, selectedMonth, selectedDay) -> {
                // Create a Calendar object and set the selected date
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
                selectedCalendar.set(Calendar.MILLISECOND, 0); // Set milliseconds to 0

                // Format the date to ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                String dob = sdf.format(selectedCalendar.getTime());

                // etEndDate the formatted date into the EditText
                etEndDate.setText(dob);
            }, year, month, day
            );

            datePickerDialog.show();
        });
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
        String driverId = tvDriverId.getText().toString().trim();
        String vehicleId = selectedVehicleId;
        int pricePerSeat = Integer.parseInt(etPricePerSeat.getText().toString().trim());
        int maxSeat = Integer.parseInt(etMaxSeat.getText().toString().trim());
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        String startLocation = etStartLocation.getText().toString().trim();
        String endLocation = etEndLocation.getText().toString().trim();
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
    private void showRouteOnMap() {
        String startLoc = etStartLocation.getText().toString().trim();
        String endLoc = etEndLocation.getText().toString().trim();

        GeoPoint startPoint = getLocationFromAddress(this, startLoc);
        GeoPoint endPoint = getLocationFromAddress(this, endLoc);

        if (startPoint == null || endPoint == null) {
            Toast.makeText(this, "Unable to find location(s).", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("StartPoint", "Lat: " + startPoint.getLatitude() + ", Lon: " + startPoint.getLongitude());
        Log.d("EndPoint", "Lat: " + endPoint.getLatitude() + ", Lon: " + endPoint.getLongitude());

        // Center map to start point
        mapView.getController().setCenter(startPoint);

        // Construct the OSRM API URL
        String routeUrl = "http://router.project-osrm.org/route/v1/driving/"
                + startPoint.getLongitude() + "," + startPoint.getLatitude() + ";"
                + endPoint.getLongitude() + "," + endPoint.getLatitude()
                + "?overview=full&geometries=polyline";

        new GetRouteTask().execute(routeUrl);
    }
    private GeoPoint getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        GeoPoint point = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null || address.size() == 0) {
                return null;
            }
            Address location = address.get(0);
            point = new GeoPoint(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return point;
    }

    private class GetRouteTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String urlStr = params[0];

            try {
                URL url = new URL(urlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("User-Agent", "MyAppName/1.0 (Android)");
                urlConnection.connect();

                InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());
                StringBuilder response = new StringBuilder();
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    response.append(current);
                    data = reader.read();
                }
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    JSONArray routes = jsonResponse.optJSONArray("routes");
                    if (routes != null && routes.length() > 0) {
                        JSONObject route = routes.getJSONObject(0);
                        String geometry = route.optString("geometry");
                        Polyline line = decodePolyline(geometry);
                        mapView.getOverlays().clear(); // Clear old routes
                        mapView.getOverlays().add(line);
                        mapView.invalidate();

                        double distance = route.optDouble("distance") / 1000.0;
                        double duration = route.optDouble("duration") / 60.0;
                        tvDistance.setText(String.format("%.2f km", distance));
                        tvDuration.setText(String.format("%.1f mins", duration));
                        Toast.makeText(AddTripActivity.this, "Distance: " + distance + " km\nDuration: " + duration + " min", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error", "Failed to get route " + e);
                    Toast.makeText(AddTripActivity.this, "Failed to parse route", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Error", "Failed to get route " + result);
                Toast.makeText(AddTripActivity.this, "Failed to get route", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Method to decode polyline geometry into coordinates
    private Polyline decodePolyline(String encoded) {
        Polyline polyline = new Polyline();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < encoded.length()) {
            int shift = 0;
            int result = 0;
            while (true) {
                int b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
                if (b < 0x20) break;
            }
            int dLat = result % 2 != 0 ? ~(result >> 1) : (result >> 1);
            lat += dLat;

            shift = 0;
            result = 0;
            while (true) {
                int b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
                if (b < 0x20) break;
            }
            int dLng = result % 2 != 0 ? ~(result >> 1) : (result >> 1);
            lng += dLng;

            GeoPoint point = new GeoPoint((lat / 1E5), (lng / 1E5));
            polyline.addPoint(point);

            // Debugging: log each decoded point
            Log.d("Decoded Point", "Latitude: " + (lat / 1E5) + ", Longitude: " + (lng / 1E5));
        }

        return polyline;
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
                    tvDriverId.setText(user.getDriver().getDriverID() != null ? user.getDriver().getDriverID() : "N/A");
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
}
