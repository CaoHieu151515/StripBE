package com.example.strip.Activities.Driver;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.strip.Models.Response.ConfirmDriverResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConfirmDriverOneActivity extends AppCompatActivity {
    private EditText etFullName, etLastName, etPhone, etEmail;
    private Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_driver_one);
        etFullName = findViewById(R.id.etFullName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        btnNext = findViewById(R.id.btnNext);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService userService = retrofit.create(IUserMobileApiService.class);

        Call<ConfirmDriverResponse> call = userService.getConfirmDriver();

        call.enqueue(new Callback<ConfirmDriverResponse>() {
            @Override
            public void onResponse(Call<ConfirmDriverResponse> call, Response<ConfirmDriverResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ConfirmDriverResponse data = response.body();
                    etFullName.setText("" + data.getFirstName());
                    etLastName.setText("" + data.getLastName());
                    etPhone.setText("" + data.getPhone());
                    etEmail.setText("" + data.getEmail());
                    // ✅ Load ảnh đơn giản hơn nhiều
                    Log.d("ImageDebug2", "Image URL: " + data.getDriverLicenseUrl());

                } else {
                    Toast.makeText(ConfirmDriverOneActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConfirmDriverResponse> call, Throwable t) {
                Toast.makeText(ConfirmDriverOneActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConfirmDriverThreeActivity.class);
            intent.putExtra("firstName", etFullName.getText().toString());
            intent.putExtra("lastName", etLastName.getText().toString());
            intent.putExtra("phone", etPhone.getText().toString());
            startActivity(intent);
            finish();
        });
    }
}
