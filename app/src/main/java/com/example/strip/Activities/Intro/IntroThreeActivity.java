package com.example.strip.Activities.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strip.Activities.StripActivity;
import com.example.strip.R;

public class IntroThreeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wp3);

        // Find the ImageView by its ID
        ImageView ivNext = findViewById(R.id.btnNext);
        ImageView btnBack = findViewById(R.id.backButton);
        // Set an OnClickListener for the ImageView
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                Intent intent = new Intent(IntroThreeActivity.this, IntroFourActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() if you want to close the IntroductionActivity
            }
        });
        TextView tvSkip = findViewById(R.id.tvSkip);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                Intent intent = new Intent(IntroThreeActivity.this, StripActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() if you want to close the IntroductionActivity
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroThreeActivity.this, IntroTwoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
