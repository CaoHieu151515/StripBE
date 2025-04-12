package com.example.strip.Activities.Trip;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Activities.Driver.AddTripActivity;
import com.example.strip.Adapters.TripStopAdapter;
import com.example.strip.Models.DriverVehicleDTO;
import com.example.strip.Models.Request.StopLocationUpdateRequest;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.Models.StopLocation;
import com.example.strip.Models.TripDetail;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.UnsafeOkHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditTripActivity extends AppCompatActivity {
    private ITripMobileApiService tripService;
    private TextView tvStartLocation, tvEndLocation;
    private String tripId;
    private RecyclerView recyclerTripStops;
    private String lastClicked = "";
    private String selectedStartLocaId = "", selectedEndLocaId = "";
    private List<StopLocationUpdateRequest> tempStopList = new ArrayList<>();
    private TripStopAdapter tripStopAdapter;
    private MapView mapView;

    // Inside EditTripActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        tvStartLocation = findViewById(R.id.tvStartLocation);
        tvEndLocation = findViewById(R.id.tvEndLocation);
        recyclerTripStops = findViewById(R.id.recyclerTripStops);

        EditText edtStopLoca = findViewById(R.id.edtStopLoca);
        EditText edtStopLocaTime = findViewById(R.id.edtStopLocaTime);
        EditText edtEstimatedTime = findViewById(R.id.edtEstimatedTime);
        EditText edtEstimatedKM = findViewById(R.id.edtEstimatedKM);
        EditText edtStopLocaPosition = findViewById(R.id.edtStopLocaPosition);

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
                    this, (view, selectedYear, selectedMonth, selectedDay) -> {
                // Create a Calendar object and set the selected date
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
                selectedCalendar.set(Calendar.MILLISECOND, 0); // Set milliseconds to 0

                // Format the date to ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                String dob = sdf.format(selectedCalendar.getTime());

                // Set the formatted date into the EditText
                edtStopLocaTime.setText(dob);
            }, year, month, day
            );

            datePickerDialog.show();
        });
        recyclerTripStops.setAdapter(tripStopAdapter);

        tripId = getIntent().getStringExtra("tripId");
        if (tripId == null) {
            Toast.makeText(this, "Invalid Trip ID!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        loadTripDetails();

        Button btnUpdateLocations = findViewById(R.id.btnUpdateLocations);
        btnUpdateLocations.setOnClickListener(v -> {
            if (tempStopList.isEmpty()) {
                Toast.makeText(this, "Chưa có điểm dừng nào để cập nhật!", Toast.LENGTH_SHORT).show();
                return;
            }
            updateTripStops();
        });

        Button btnAddStop = findViewById(R.id.btnAddStop);
        btnAddStop.setOnClickListener(v -> {
            try {
                StopLocationUpdateRequest newStop = new StopLocationUpdateRequest();
                newStop.setStopLoca(edtStopLoca.getText().toString());
                newStop.setStopLocaTime(edtStopLocaTime.getText().toString());
                newStop.setStopLocaStatus("UPCOMMING");
                newStop.setEstimatedTime(Integer.parseInt(edtEstimatedTime.getText().toString()));
                newStop.setEstimatedKM(Double.parseDouble(edtEstimatedKM.getText().toString()));
                newStop.setStoplocaPosition(Integer.parseInt(edtStopLocaPosition.getText().toString()));

                tempStopList.add(newStop); // for updateTripStops()

                // Convert to StopLocation for display
                StopLocation displayStop = new StopLocation();
                displayStop.setStopLoca(newStop.getStopLoca());
                displayStop.setStopLocaTime(newStop.getStopLocaTime());
                displayStop.setStopLocaStatus(newStop.getStopLocaStatus());
                displayStop.setEstimatedTime(newStop.getEstimatedTime());
                displayStop.setEstimatedKM(newStop.getEstimatedKM());
                displayStop.setStoplocaPosition(newStop.getStoplocaPosition());

                // Get current adapter list
                List<StopLocation> currentList = ((TripStopAdapter) recyclerTripStops.getAdapter()).getStopList();
                currentList.add(displayStop);
                tripStopAdapter.notifyItemInserted(currentList.size() - 1);
                loadTripDetails();
            } catch (Exception ex) {
                Toast.makeText(this, "Invalid stop info: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Retrofit getRetrofitClient() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);
        if (jwtToken == null) {
            Toast.makeText(EditTripActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
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
    private void loadTripDetails() {
        tripService = getRetrofitClient().create(ITripMobileApiService.class);
        tripService.getTripDetails(tripId).enqueue(new Callback<TripDetail>() {
            @Override
            public void onResponse(@NonNull Call<TripDetail> call, @NonNull Response<TripDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TripDetail trip = response.body();
                    tvStartLocation.setText(trip.getStartLocation());
                    tvEndLocation.setText(trip.getEndLocation());
                    List<StopLocation> stops = trip.getStopLocations();
                    if (stops != null && !stops.isEmpty()) {
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
//                        edtEstimatedTime.setText(String.format("%.2f km", distance));
//                        edtEstimatedTime.tvDuration.setText(String.format("%.1f mins", duration));
                        Toast.makeText(EditTripActivity.this, "Distance: " + distance + " km\nDuration: " + duration + " min", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error", "Failed to get route " + e);
                    Toast.makeText(EditTripActivity.this, "Failed to parse route", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Error", "Failed to get route " + result);
                Toast.makeText(EditTripActivity.this, "Failed to get route", Toast.LENGTH_SHORT).show();
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

}
