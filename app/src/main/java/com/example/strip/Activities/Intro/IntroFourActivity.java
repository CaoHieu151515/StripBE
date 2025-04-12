package com.example.strip.Activities.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strip.R;

public class IntroFourActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wp4); // Update with your actual XML file name

        CheckBox cbAgree = findViewById(R.id.cbAgree);
        ImageView btnBack = findViewById(R.id.backButton);
        LinearLayout checkboxContainer = findViewById(R.id.checkboxContainer);

        cbAgree.setOnClickListener(v -> checkboxContainer.performClick());

        checkboxContainer.setOnClickListener(v -> {
            cbAgree.setChecked(!cbAgree.isChecked());
            if (cbAgree.isChecked()) { // Navigate when checkbox is checked
                Intent intent = new Intent(IntroFourActivity.this, IntroFiveActivity.class); // Replace 'NextActivity' with your target activity
                startActivity(intent);
                finish();
            }
            // Additional action if needed when clicking checkboxContainer
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroFourActivity.this, IntroThreeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
