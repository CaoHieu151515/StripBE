package com.example.strip.Activities.Driver;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.strip.Models.Response.ConfirmDriverResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.network.ApiClient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConfirmDriverOneActivity extends AppCompatActivity {
    private EditText etFullName, etLastName, etPhone, etEmail;
    private ImageView licenseImageView;
    private Button btnNext;
    private static final int REQUEST_IMAGE_PICK = 100;
    private byte[] licenseImageBytes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_driver_one);
        etFullName = findViewById(R.id.etFullName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        licenseImageView = findViewById(R.id.licenseImageView);
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
                    loadImageWithFixHost(data.getDriverLicenseUrl(), licenseImageView);
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
            Intent intent = new Intent(this, ConfirmDriverTwoActivity.class);
            intent.putExtra("firstName", etFullName.getText().toString());
            intent.putExtra("lastName", etLastName.getText().toString());
            intent.putExtra("phone", etPhone.getText().toString());
            intent.putExtra("driverLicense", licenseImageBytes);
            startActivity(intent);
        });
        licenseImageView.setOnClickListener(v -> {
            openImagePicker();
        });
    }
    private void loadImageWithFixHost(String url, ImageView target) {
        if (url != null && !url.isEmpty()) {
            if (url.contains("localhost")) {
                url = url.replace("https://localhost", "http://10.0.2.2:8080");
            }
            if (!ConfirmDriverOneActivity.this.isFinishing()
                    && !ConfirmDriverOneActivity.this.isDestroyed()) {
                Glide.with(this)
                        .load(url)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logout)
                        .skipMemoryCache(true) // Skip memory cache
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip disk cache
                        .into(target);

            }
            Log.d("ImageDebug", "Image URL: " + url);

        }
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_PICK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                licenseImageView.setImageURI(selectedImageUri);

                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    if (inputStream == null) {
                        throw new IOException("InputStream is null");
                    }

// decode bounds first
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(inputStream, null, options);
                    inputStream.close();

// calculate sample size
                    int scale = 1;
                    while (options.outWidth / scale > 800 || options.outHeight / scale > 800) {
                        scale *= 2;
                    }

// decode actual bitmap
                    inputStream = getContentResolver().openInputStream(selectedImageUri);
                    BitmapFactory.Options finalOptions = new BitmapFactory.Options();
                    finalOptions.inSampleSize = scale;
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, finalOptions);
                    inputStream.close();

                    licenseImageView.setImageBitmap(bitmap);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    licenseImageBytes = stream.toByteArray();

                    Log.d("ImageBytes", "Byte array size: " + licenseImageBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to process image!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
