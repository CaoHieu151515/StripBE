package com.example.strip.Activities.Customer;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.strip.Activities.Account.LoginActivity;
import com.example.strip.Activities.Driver.RatingFeedbackDriverActivity;
import com.example.strip.Activities.StripActivity;
import com.example.strip.Models.Request.PassengerProfileRequest;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import im.crisp.client.internal.j.n;
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
    private NotificationPopup notificationPopup;
    private String formatTimeShow, formatTimeStore;
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
        notificationPopup = new NotificationPopup(this);
        Button btnSave = findViewById(R.id.btnSave);

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent != null) {
            String imageUrl = intent.getStringExtra("imageUrl");
            if (imageUrl != null && !imageUrl.isEmpty()) {
                if (imageUrl.contains("localhost")) {
                    imageUrl = imageUrl.replace("https://localhost", "http://10.0.2.2:8080");
                }
                if (!EditProfilePassengerActivity.this.isFinishing()
                        && !EditProfilePassengerActivity.this.isDestroyed()) {
                    Glide.with(EditProfilePassengerActivity.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.logout)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(ivProfile);;
                }

            }
            etFirstName.setText(intent.getStringExtra("firstName"));
            etLastName.setText(intent.getStringExtra("lastName"));
            etPhone.setText(intent.getStringExtra("phone"));
            etAddress.setText(intent.getStringExtra("address"));
            String isoDob = intent.getStringExtra("dob");
            if (isoDob != null && !isoDob.isEmpty()) {
                try {
                    // Step 1: Parse the ISO 8601 date
                    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                    isoFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Important if your date has Z (UTC) suffix
                    Date date = isoFormat.parse(isoDob);

                    // Step 2: Format to dd/MM/yyyy
                    SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDob = displayFormat.format(date);

                    // Step 3: Set it to the EditText
                    etDob.setText(formattedDob);

                } catch (ParseException e) {
                    e.printStackTrace();
                    etDob.setText("Invalid date");
                }
            }

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
                SimpleDateFormat sdfStore = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                formatTimeStore = sdfStore.format(selectedCalendar.getTime());
                SimpleDateFormat sdfShow = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                formatTimeShow = sdfShow.format(selectedCalendar.getTime());
                // Set the formatted date into the EditText
                etDob.setText(formatTimeShow);
            }, year, month, day
            );

            datePickerDialog.show();
        });

        etGender.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn giới tính");

            String[] genders = {"Nam", "Nữ"};
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

                    ivProfile.setImageBitmap(bitmap);

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

    private void updateUserProfile() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String phone = etPhone.getText().toString();
        String address = etAddress.getText().toString();
        if (etDob.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid date of birth!", Toast.LENGTH_SHORT).show();
            return;
        }
        String dob = formatTimeStore;
        String gender = etGender.getText().toString();
        String userImageContentType = "image/png";
        byte[] userImage = userImageBytes;
        // Dummy image URL, replace with actual image handling
        Retrofit retrofit = ApiClient.getClientWithToken(this);
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
                    notificationPopup.showPopup("Cập nhật hồ sơ thành công!",false);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        finish();
                    }, 1500);
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e("Failed:", "Update failed! Code: " + response.code());
                        Log.e("Failed:", "Update failed! Body: " + errorBody);
                    } catch (IOException e) {
                        Log.e("Failed:", "Error reading error body", e);
                    }
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
