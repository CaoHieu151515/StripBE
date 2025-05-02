package com.example.strip.Activities.Trip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Activities.OpenStreetMapActivity;
import com.example.strip.Adapters.LocationAdapter;
import com.example.strip.Models.LocationInfo;
import com.example.strip.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TripStoreActivity extends AppCompatActivity {
    private EditText etStartLocation ,etEndLocation;
    private Button btnSearchLocation, btnChooseLocation, btnStore;
    private ImageView ivExit;
    private MapView mapView;
    private TextView tvDistanceValue, tvDurationValue;
    private double distance;
    private int duration;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_trip);
        etStartLocation = findViewById(R.id.etStartLocation);
        etEndLocation = findViewById(R.id.etEndLocation);
        btnSearchLocation = findViewById(R.id.btnSearchLocation);
        btnChooseLocation = findViewById(R.id.btnChooseLocation);
        btnStore = findViewById(R.id.btnStore);
        tvDistanceValue = findViewById(R.id.tvDistanceValue);
        tvDurationValue = findViewById(R.id.tvDurationValue);
        ivExit = findViewById(R.id.ivExit);

        recyclerView = findViewById(R.id.recyclerView); // <-- Add this line!
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<LocationInfo> trips = loadLocationInfos();
        LocationAdapter adapter = new LocationAdapter(trips, new LocationAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDeleted(int position, List<LocationInfo> updatedList) {
                // Save updated list to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("LOCATION_PREFS", MODE_PRIVATE);
                Gson gson = new Gson();
                String updatedJson = gson.toJson(updatedList);
                prefs.edit().putString("location_list", updatedJson).apply();
                loadLocationInfos();
            }
        });
        recyclerView.setAdapter(adapter);
        etStartLocation.setText(getIntent().getStringExtra("startLocation"));
        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start = etStartLocation.getText().toString();
                String end = etEndLocation.getText().toString();
                String distance = tvDistanceValue.getText().toString();
                String duration = tvDurationValue.getText().toString();

                LocationInfo trip = new LocationInfo(start, end, distance, duration);
                saveLocationInfo(trip);

                // Reload list from SharedPreferences and update adapter
                List<LocationInfo> updatedList = loadLocationInfos();
                adapter.updateData(updatedList);
            }
        });

        Configuration.getInstance().setUserAgentValue("MyAppName/1.0 (Android)");

        // Initialize MapView
        mapView = findViewById(R.id.mapViewOSM);
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Set initial location of the map (e.g., Berlin)
        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(new GeoPoint(10.7769, 106.7009));
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRouteOnMap();
            }
        });
        btnChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("startLocation", etStartLocation.getText().toString().trim());
                resultIntent.putExtra("endLocation", etEndLocation.getText().toString().trim());
                resultIntent.putExtra("distance", distance);
                resultIntent.putExtra("duration", duration);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
    private void saveLocationInfo(LocationInfo info) {
        SharedPreferences prefs = getSharedPreferences("LOCATION_PREFS", MODE_PRIVATE);
        Gson gson = new Gson();

        // Load existing list
        String json = prefs.getString("location_list", "[]");
        Type type = new TypeToken<ArrayList<LocationInfo>>(){}.getType();
        List<LocationInfo> list = gson.fromJson(json, type);

        // Add new location info
        list.add(info);

        // Save updated list
        String updatedJson = gson.toJson(list);
        prefs.edit().putString("location_list", updatedJson).apply();
    }

    private List<LocationInfo> loadLocationInfos() {
        SharedPreferences prefs = getSharedPreferences("LOCATION_PREFS", MODE_PRIVATE);
        String json = prefs.getString("location_list", "[]");
        Type type = new TypeToken<ArrayList<LocationInfo>>(){}.getType();
        return new Gson().fromJson(json, type);
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

        new TripStoreActivity.GetRouteTask().execute(routeUrl);
        tvDistanceValue.setText(String.format("%.2f km", distance));
        tvDurationValue.setText(String.format("%d mins", duration));
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

                        distance = route.optDouble("distance") / 1000.0;
                        duration = route.optInt("duration") / 60;
                        tvDistanceValue.setText(String.format("%.2f km", distance));
                        tvDurationValue.setText(duration + " mins");
                        Toast.makeText(TripStoreActivity.this, "Distance: " + distance + " km\nDuration: " + duration + " min", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error", "Failed to get route " + e);
                    Toast.makeText(TripStoreActivity.this, "Failed to parse route", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Error", "Failed to get route " + result);
                Toast.makeText(TripStoreActivity.this, "Failed to get route", Toast.LENGTH_SHORT).show();
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
