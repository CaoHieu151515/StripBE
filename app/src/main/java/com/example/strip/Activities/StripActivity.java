package com.example.strip.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.strip.Activities.Account.BeginActivity;
import com.example.strip.Activities.Account.LoginActivity;
import com.example.strip.Activities.Driver.DriverProfileActivity;
import com.example.strip.Activities.Driver.ManageTripActivity;
import com.example.strip.Activities.Driver.WalletActivity;
import com.example.strip.Fragments.AccountFragment;
import com.example.strip.Fragments.ChatFragment;
import com.example.strip.Fragments.HomeFragment;
import com.example.strip.Fragments.TripsFragment;
import com.example.strip.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import im.crisp.client.external.Crisp;

public class StripActivity extends AppCompatActivity {
//    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        fab = findViewById(R.id.fab);
//        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment()).commit();
        }
        replaceFragment(new HomeFragment());
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.trips) {
                replaceFragment(new TripsFragment());
            } else if (item.getItemId() == R.id.chat) {
                replaceFragment(new ChatFragment());
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(new AccountFragment());
            }
            return true;
        });
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showBottomDialog();
//            }
//        });

        Crisp.resetChatSession(StripActivity.this.getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);
        if (jwtToken != null) {
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            String email = decodedJWT.getClaim("sub").asString(); // Extract user email or other claims
            String auth = decodedJWT.getClaim("auth").asString();
            // Set Crisp user information for the new session
            Crisp.configure(StripActivity.this.getApplicationContext(), "ea85235a-fdd6-4b5c-ab9d-a0d3fc0aac72",jwtToken);
            Crisp.setUserEmail(email);
        }

//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if (item.getItemId() == R.id.nav_logout) {
//                    Log.d("LogoutDebug", "Logout menu item selected");
//                    handleLogout();
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }
    public void handleLogout() {
        // Clear chat data for the current user
        Crisp.resetChatSession(StripActivity.this.getApplicationContext());

        // Continue with logout logic, like clearing user data and redirecting to login
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("jwtToken"); // Clear any saved user authentication data
        editor.apply();

        Intent intent = new Intent(StripActivity.this, BeginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_logout) {
            Log.d("LogoutDebug", "Logout menu item selected");

            handleLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    private void showBottomDialog() {
//
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.bottom_sheet_layout);
//
//        LinearLayout layoutTrip = dialog.findViewById(R.id.layoutTrip);
//        LinearLayout layoutWallet = dialog.findViewById(R.id.layoutWallet);
//        LinearLayout layoutProfile = dialog.findViewById(R.id.layoutProfile);
//        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
//
//        layoutTrip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//                // Create an Intent to start the new activity
//                Intent intent = new Intent(StripActivity.this, ManageTripActivity.class);
//
//                // Start the new activity
//                startActivity(intent);
//
//                Toast.makeText(StripActivity.this,"Payment is clicked",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        layoutWallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Intent intent = new Intent(StripActivity.this, WalletActivity.class);
//                startActivity(intent);
//                Toast.makeText(StripActivity.this,"Wallet is Clicked",Toast.LENGTH_SHORT).show();
//
//            }
//        });
////
//        layoutProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//                Intent intent = new Intent(StripActivity.this, DriverProfileActivity.class);
//                startActivity(intent);
//                Toast.makeText(StripActivity.this,"Driver profile is Clicked",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
//
//    }
}
