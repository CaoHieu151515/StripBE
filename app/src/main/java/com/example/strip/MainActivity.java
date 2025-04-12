package com.example.strip;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.util.GeoPoint;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a custom user-agent for OSMDroid
        Configuration.getInstance().setUserAgentValue("MyAppName/1.0 (Android)");

        // Initialize MapView
        mapView = findViewById(R.id.mapview);
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Set initial location of the map (e.g., Berlin)
        mapView.getController().setZoom(12);
        mapView.getController().setCenter(new GeoPoint(21.0285, 105.8542)); // Initial coordinates (latitude, longitude)

        // Get the route from OSRM API
        String startLat = "21.0285";  // Hanoi Latitude
        String startLon = "105.8542"; // Hanoi Longitude
        String endLat = "20.8443";    // Haiphong Latitude
        String endLon = "106.6882";   // Haiphong Longitude

        new GetRouteTask().execute(startLat, startLon, endLat, endLon);
    }

    // AsyncTask to get route from OSRM API
    private class GetRouteTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String startLat = "21.0285";  // Hanoi Latitude
            String startLon = "105.8542"; // Hanoi Longitude
            String endLat = "20.8443";    // Haiphong Latitude
            String endLon = "106.6882";   // Haiphong Longitude

            try {
                // Construct the OSRM API URL for route calculation
                String urlStr = "http://router.project-osrm.org/route/v1/driving/"
                        + startLon + "," + startLat + ";" + endLon + "," + endLat
                        + "?overview=false&steps=true";
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

                return response.toString(); // Return the response from the API
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    Log.d("API Response", result);

                    JSONObject jsonResponse = new JSONObject(result);
                    if (jsonResponse.getString("code").equals("Ok")) {
                        JSONArray routes = jsonResponse.optJSONArray("routes");
                        if (routes != null && routes.length() > 0) {
                            JSONObject route = routes.optJSONObject(0);
                            JSONArray legs = route.optJSONArray("legs");

                            if (legs != null && legs.length() > 0) {
                                JSONObject leg = legs.optJSONObject(0);
                                JSONArray steps = leg.optJSONArray("steps");

                                if (steps != null) {
                                    for (int i = 0; i < steps.length(); i++) {
                                        JSONObject step = steps.optJSONObject(i);
                                        String geometry = step.optString("geometry");

                                        if (!geometry.isEmpty()) {
                                            Polyline polyline = decodePolyline(geometry);
                                            mapView.getOverlays().add(polyline);
                                            mapView.invalidate();
                                        }
                                    }

                                    // Zoom to fit the polyline on the map

//                                    mapView.zoomToBoundingBox(polyline.getBoundingBox(), true);
                                }

                                // Show distance and time
                                double distance = route.optDouble("distance", 0) / 1000; // Convert to km
                                double duration = route.optDouble("duration", 0) / 60; // Convert to minutes
                                Toast.makeText(MainActivity.this, "Distance: " + distance + " km\nDuration: " + duration + " min", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Error: " + jsonResponse.optString("code"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("API Response", result);
                    Toast.makeText(MainActivity.this, "Error parsing route data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Failed to get route data", Toast.LENGTH_SHORT).show();
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