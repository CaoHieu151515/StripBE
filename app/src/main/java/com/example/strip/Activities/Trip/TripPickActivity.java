package com.example.strip.Activities.Trip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strip.R;

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
import java.util.List;

public class TripPickActivity extends AppCompatActivity {
    private MapView mapView;
    private TextView tvStartLocation, tvEndLocation;
    private ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_driver); // Update with your actual XML file name
        tvStartLocation = findViewById(R.id.tvStartLocation);
        tvEndLocation = findViewById(R.id.tvEndLocation);
        imgProfile = findViewById(R.id.imgProfile);
        Configuration.getInstance().setUserAgentValue("MyAppName/1.0 (Android)");

        // Initialize MapView
        mapView = findViewById(R.id.mapViewOSM);
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Set initial location of the map (e.g., Berlin)
        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(new GeoPoint(10.7769, 106.7009));
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        showRouteOnMap();
    }
    private void showRouteOnMap() {
        String startLoc = tvStartLocation.getText().toString().trim();
        String endLoc = tvEndLocation.getText().toString().trim();

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

        new TripPickActivity.GetRouteTask().execute(routeUrl);
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
                        Toast.makeText(TripPickActivity.this, "Distance: " + distance + " km\nDuration: " + duration + " min", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error", "Failed to get route " + e);
                    Toast.makeText(TripPickActivity.this, "Failed to parse route", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Error", "Failed to get route " + result);
                Toast.makeText(TripPickActivity.this, "Failed to get route", Toast.LENGTH_SHORT).show();
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
