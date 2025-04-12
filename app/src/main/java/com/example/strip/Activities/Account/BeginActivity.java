package com.example.strip.Activities.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strip.Activities.Intro.IntroFiveActivity;
import com.example.strip.Activities.Intro.IntroFourActivity;
import com.example.strip.Activities.StripActivity;
import com.example.strip.R;

public class BeginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        // Find the ImageView by its ID
        Button btnLogin = findViewById(R.id.buttonSignIn);
        Button btnRegister = findViewById(R.id.buttonSignUp);

        // Set an OnClickListener for the ImageView
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                Intent intent = new Intent(BeginActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() if you want to close the IntroductionActivity
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                Intent intent = new Intent(BeginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() if you want to close the IntroductionActivity
            }
        });
    }
}
