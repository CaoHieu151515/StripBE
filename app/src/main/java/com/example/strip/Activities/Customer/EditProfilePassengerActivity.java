package com.example.strip.Activities.Customer;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.strip.Models.Request.PassengerProfileRequest;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.UnsafeOkHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfilePassengerActivity extends AppCompatActivity {
    private EditText etFirstName, etLastName, etPhone, etAddress, etDob, etGender;
    private ImageView ivProfile, ivCamera;
    private byte[] userImageBytes;
    private static final int REQUEST_IMAGE_PICK = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_passenger);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etDob = findViewById(R.id.etDob);
        etGender = findViewById(R.id.etGender);
        ivProfile = findViewById(R.id.ivProfile);
        ivCamera = findViewById(R.id.ivCamera);
        Button btnSave = findViewById(R.id.btnSave);

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent != null) {
            String imageUrl = intent.getStringExtra("imageUrl");
            if (imageUrl != null && !imageUrl.isEmpty()) {
                if (imageUrl.startsWith("http://localhost")) {
                    imageUrl = imageUrl.replace("http://localhost", "http://10.0.2.2");
                }
                Glide.with(EditProfilePassengerActivity.this)
                        .load(imageUrl)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logout)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(ivProfile);;
            }
            etFirstName.setText(intent.getStringExtra("firstName"));
            etLastName.setText(intent.getStringExtra("lastName"));
            etPhone.setText(intent.getStringExtra("phone"));
            etAddress.setText(intent.getStringExtra("address"));
            etDob.setText(intent.getStringExtra("dob"));
            etGender.setText(intent.getStringExtra("gender"));
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, (view, selectedYear, selectedMonth, selectedDay) -> {
                // Create a Calendar object and set the selected date
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
                selectedCalendar.set(Calendar.MILLISECOND, 0); // Set milliseconds to 0

                // Format the date to ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                String dob = sdf.format(selectedCalendar.getTime());

                // Set the formatted date into the EditText
                etDob.setText(dob);
            }, year, month, day
            );

            datePickerDialog.show();
        });

        etGender.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Gender");

            String[] genders = {"Male", "Female"};
            builder.setItems(genders, (dialog, which) -> {
                etGender.setText(genders[which]);
            });

            builder.show();
        });

        ivCamera.setOnClickListener(v -> {
            openImagePicker();
        });
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
                ivProfile.setImageURI(selectedImageUri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    userImageBytes = stream.toByteArray();
                    Log.d("ImageBytes", "Byte array size: " + userImageBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to process image!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private Retrofit getRetrofitClient() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);
        if (jwtToken == null) {
            Toast.makeText(EditProfilePassengerActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
        }
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                .newBuilder()
                .addInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    if (jwtToken != null) {
                        requestBuilder.addHeader("Authorization", "Bearer " + jwtToken);
                    }
                    return chain.proceed(requestBuilder.build());
                })
                .build();


        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    private void updateUserProfile() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String phone = etPhone.getText().toString();
        String address = etAddress.getText().toString();
        if (etDob.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid date of birth!", Toast.LENGTH_SHORT).show();
            return;
        }
        String dob = etDob.getText().toString().trim();
        String gender = etGender.getText().toString();
        String userImageContentType = "image/png";
        byte[] userImage = userImageBytes;
        // Dummy image URL, replace with actual image handling
        Retrofit retrofit = getRetrofitClient();
        IUserMobileApiService apiService = retrofit.create(IUserMobileApiService.class);

        PassengerProfileRequest passengerProfileRequest = new PassengerProfileRequest(
                firstName,
                lastName,
                phone,
                dob,
                address,
                gender,
                userImageContentType,
                userImage
        );
        Call<Void> call = apiService.updateProfile(passengerProfileRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    finish();
                    Toast.makeText(EditProfilePassengerActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Failed:", "Update failed! Code: " + response.code());
                    Toast.makeText(EditProfilePassengerActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Error", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(EditProfilePassengerActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
