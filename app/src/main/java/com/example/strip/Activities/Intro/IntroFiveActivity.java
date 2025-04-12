package com.example.strip.Activities.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strip.Activities.Account.BeginActivity;
import com.example.strip.Activities.StripActivity;
import com.example.strip.R;

public class IntroFiveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wp5);
        ImageView btnBack = findViewById(R.id.backButton);
        // Find the ImageView by its ID
        Button btnStart = findViewById(R.id.startButton);
        // Set an OnClickListener for the ImageView
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                Intent intent = new Intent(IntroFiveActivity.this, BeginActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() if you want to close the IntroductionActivity
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroFiveActivity.this, IntroFourActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
