package com.example.strip.Activities.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strip.Activities.Account.BeginActivity;
import com.example.strip.R;

public class IntroTwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wp2);

        // Find the ImageView by its ID
        ImageView ivNext = findViewById(R.id.btnNext);
        ImageView btnBack = findViewById(R.id.backButton);
        TextView tvSkip = findViewById(R.id.tvSkip);
        // Set an OnClickListener for the ImageView
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                Intent intent = new Intent(IntroTwoActivity.this, IntroThreeActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() if you want to close the IntroductionActivity
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                Intent intent = new Intent(IntroTwoActivity.this, BeginActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() if you want to close the IntroductionActivity
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroTwoActivity.this, IntroOneActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
