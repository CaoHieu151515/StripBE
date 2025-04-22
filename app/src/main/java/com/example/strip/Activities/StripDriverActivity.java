package com.example.strip.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.strip.Fragments.DriverProfileFragment;
import com.example.strip.Fragments.HomeDriverFragment;
import com.example.strip.Fragments.HomeFragment;
import com.example.strip.Fragments.ManageTripsFragment;
import com.example.strip.Fragments.ManageWalletsFragment;
import com.example.strip.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import im.crisp.client.external.Crisp;

public class StripDriverActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_driver);

        bottomNavigationView = findViewById(R.id.bottomNavigationViewDriver);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment()).commit();
        }

        replaceFragment(new HomeFragment());
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.homeDriver) {
                replaceFragment(new HomeDriverFragment());
            } else if (item.getItemId() == R.id.tripsDriver) {
                replaceFragment(new ManageTripsFragment());
            } else if (item.getItemId() == R.id.walletsDriver) {
                replaceFragment(new ManageWalletsFragment());
            } else if (item.getItemId() == R.id.driver) {
                replaceFragment(new DriverProfileFragment());
            }
            return true;
        });


        Crisp.resetChatSession(StripDriverActivity.this.getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);
        if (jwtToken != null) {
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            String email = decodedJWT.getClaim("sub").asString(); // Extract user email or other claims
//            String auth = decodedJWT.getClaim("auth").asString();
            // Set Crisp user information for the new session
            Crisp.configure(StripDriverActivity.this.getApplicationContext(), "ea85235a-fdd6-4b5c-ab9d-a0d3fc0aac72",jwtToken);
            Crisp.setUserEmail(email);
        }

    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

}
