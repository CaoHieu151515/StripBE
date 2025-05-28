package com.example.strip.Activities.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.strip.Activities.Account.BeginActivity;
import com.example.strip.R;

public class IntroOneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wp1);

        // Find the ImageView by its ID
        ImageView ivNext = findViewById(R.id.btnNext);
        TextView tvSkip = findViewById(R.id.tvSkip);

        // Set an OnClickListener for the ImageView
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                Intent intent = new Intent(IntroOneActivity.this, IntroTwoActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() if you want to close the IntroductionActivity
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                Intent intent = new Intent(IntroOneActivity.this, BeginActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() if you want to close the IntroductionActivity
            }
        });
    }
}
