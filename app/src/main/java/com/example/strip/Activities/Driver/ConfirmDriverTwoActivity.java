package com.example.strip.Activities.Driver;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.strip.Activities.Customer.EditProfilePassengerActivity;
import com.example.strip.Models.Response.ConfirmDriverResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.network.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConfirmDriverTwoActivity extends AppCompatActivity {
    private ImageView faceUpImageView, faceDownImageView;
    private Button btnNext;
    private static final int REQUEST_IMAGE_PICK = 100;
    private static final int REQUEST_IMAGE_TWO_PICK = 101;
    private String firstName, lastName, phone;
    private byte[] licenseImageBytes, faceUpImageBytes, faceDownImageBytes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_driver_two);
        faceUpImageView = findViewById(R.id.faceUpImageView);
        faceDownImageView = findViewById(R.id.faceDownImageView);
        btnNext = findViewById(R.id.btnNext);
        ImageView btnBack = findViewById(R.id.backButton);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConfirmDriverThreeActivity.class);
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            intent.putExtra("phone", phone);
            intent.putExtra("driverLicense", licenseImageBytes);

            intent.putExtra("identityCardFaceUp", faceUpImageBytes);
            intent.putExtra("identityCardFacedown", faceDownImageBytes);
            startActivity(intent);
        });
        Intent intent = getIntent();
        if (intent != null) {
            licenseImageBytes = intent.getByteArrayExtra("driverLicense");
            firstName = intent.getStringExtra("firstName");
            lastName = intent.getStringExtra("lastName");
            phone = intent.getStringExtra("phone");
        }
        faceUpImageView.setOnClickListener(v -> {
            openFaceUpImageViewPicker();
        });
        faceDownImageView.setOnClickListener(v -> {
            openFaceDownImageViewPicker();
        });
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService userService = retrofit.create(IUserMobileApiService.class);

        Call<ConfirmDriverResponse> call = userService.getConfirmDriver();

        call.enqueue(new Callback<ConfirmDriverResponse>() {
            @Override
            public void onResponse(Call<ConfirmDriverResponse> call, Response<ConfirmDriverResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ConfirmDriverResponse data = response.body();
                    // ✅ Load ảnh đơn giản hơn nhiều
                    loadImageWithFixHost(data.getDriverLicenseUrl(), faceUpImageView);
                    loadImageWithFixHost(data.getDriverLicenseUrl(), faceDownImageView);
                } else {
                    Toast.makeText(ConfirmDriverTwoActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConfirmDriverResponse> call, Throwable t) {
                Toast.makeText(ConfirmDriverTwoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void loadImageWithFixHost(String url, ImageView target) {
        if (url != null && !url.isEmpty()) {
            if (url.contains("localhost")) {
                url = url.replace("http://localhost", "http://10.0.2.2:8080");
            }

            Log.d("ImageDebug", "Image URL: " + url);

            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logout)
                    .into(target);
        }
    }
    private void openFaceUpImageViewPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    private void openFaceDownImageViewPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_TWO_PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_PICK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                faceUpImageView.setImageURI(selectedImageUri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    faceUpImageBytes = stream.toByteArray();
                    Log.d("ImageBytes", "Byte array size: " + faceUpImageBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to process image!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_TWO_PICK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                faceDownImageView.setImageURI(selectedImageUri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    faceDownImageBytes = stream.toByteArray();
                    Log.d("ImageBytes", "Byte array size: " + faceDownImageBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to process image!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
