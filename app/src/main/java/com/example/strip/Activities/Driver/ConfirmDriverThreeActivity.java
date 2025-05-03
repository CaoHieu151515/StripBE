package com.example.strip.Activities.Driver;

import android.app.AlertDialog;
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
import com.example.strip.Models.Request.ConfirmDriverRequest;
import com.example.strip.Models.Response.ConfirmDriverResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.network.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConfirmDriverThreeActivity extends AppCompatActivity {
    private EditText etVehicleType, etVehicleColor, etVehicleNumber, etSeats, etVehicleBrand;
    private ImageView vehicleImageView, carRegistrationImageView, inspectionCertificateImageView, insuranceImageView;
    private Button btnConfirmDriver;
    private byte[] vehicleImageBytes, carRegistrationImageBytes, inspectionCertificateImageBytes, insuranceImageBytes,
            licenseImageBytes, faceUpImageBytes, faceDownImageBytes;
    private String firstName, lastName, phone;
    private Retrofit retrofit;
    private static final int REQUEST_IMAGE_PICK = 100;
    private static final int REQUEST_IMAGE_TWO_PICK = 101;
    private static final int REQUEST_IMAGE_THREE_PICK = 102;
    private static final int REQUEST_IMAGE_FOUR_PICK = 103;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_driver_three);
        etVehicleType = findViewById(R.id.etVehicleType);
        etVehicleColor = findViewById(R.id.etVehicleColor);
        etVehicleNumber = findViewById(R.id.etVehicleNumber);
        etSeats = findViewById(R.id.etSeats);
        etVehicleBrand = findViewById(R.id.etVehicleBrand);
        vehicleImageView = findViewById(R.id.vehicleImageView);
        carRegistrationImageView = findViewById(R.id.carRegistrationImageView);
        inspectionCertificateImageView = findViewById(R.id.inspectionCertificateImageView);
        insuranceImageView = findViewById(R.id.insuranceImageView);
        btnConfirmDriver = findViewById(R.id.btnConfirmDriver);
        retrofit = ApiClient.getClientWithToken(this);
        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etVehicleType.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn Loại Xe");

            String[] genders = {"BIKE", "CAR"};
            builder.setItems(genders, (dialog, which) -> {
                etVehicleType.setText(genders[which]);
            });

            builder.show();
        });
        etVehicleBrand.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn Hãng Xe");

            String[] genders = {"Honda", "Suzuki", "Vision"};
            builder.setItems(genders, (dialog, which) -> {
                etVehicleBrand.setText(genders[which]);
            });

            builder.show();
        });
        vehicleImageView.setOnClickListener(v -> {
            openVehicleImageViewicker();
        });
        carRegistrationImageView.setOnClickListener(v -> {
            openCarRegistrationImageViewPicker();
        });
        inspectionCertificateImageView.setOnClickListener(v -> {
            openInspectionCertificateImageViewPicker();
        });
        insuranceImageView.setOnClickListener(v -> {
            openInsuranceImageViewPicker();
        });
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService userService = retrofit.create(IUserMobileApiService.class);

        Call<ConfirmDriverResponse> call = userService.getConfirmDriver();

        call.enqueue(new Callback<ConfirmDriverResponse>() {
            @Override
            public void onResponse(Call<ConfirmDriverResponse> call, Response<ConfirmDriverResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ConfirmDriverResponse data = response.body();
                    etVehicleType.setText("" + data.getVehicleResponse().getVehicleType());
                    etVehicleColor.setText("" + data.getVehicleResponse().getVehicleColor());
                    etVehicleNumber.setText("" + data.getVehicleResponse().getVehicleNumber());
                    etSeats.setText("" + data.getVehicleResponse().getNumberOfSeats());
                    etVehicleBrand.setText("" + data.getVehicleResponse().getVehicleBrand());

                    // ✅ Load ảnh đơn giản hơn nhiều
                    loadImageWithFixHost(data.getDriverLicenseUrl(), vehicleImageView);
                    loadImageWithFixHost(data.getDriverLicenseUrl(), carRegistrationImageView);
                    loadImageWithFixHost(data.getDriverLicenseUrl(), inspectionCertificateImageView);
                    loadImageWithFixHost(data.getDriverLicenseUrl(), insuranceImageView);
                } else {
                    Toast.makeText(ConfirmDriverThreeActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConfirmDriverResponse> call, Throwable t) {
                Toast.makeText(ConfirmDriverThreeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            licenseImageBytes = intent.getByteArrayExtra("driverLicense");
            firstName = intent.getStringExtra("firstName");
            lastName = intent.getStringExtra("lastName");
            phone = intent.getStringExtra("phone");

            faceUpImageBytes = intent.getByteArrayExtra("identityCardFaceUp");
            faceDownImageBytes = intent.getByteArrayExtra("identityCardFacedown");
        }
        btnConfirmDriver.setOnClickListener(v -> {
            // 2. Create request object
            ConfirmDriverRequest request = new ConfirmDriverRequest(
                    firstName, lastName, phone,
                    licenseImageBytes, faceUpImageBytes,
                    "image/png", "image/png",
                    faceDownImageBytes, "image/png",
                    Integer.parseInt(etSeats.getText().toString()), etVehicleType.getText().toString(), etVehicleNumber.getText().toString(), etVehicleColor.getText().toString(),
                    vehicleImageBytes, etVehicleBrand.getText().toString(), "image/png",
                    carRegistrationImageBytes, "image/png",
                    inspectionCertificateImageBytes, "image/png",
                    insuranceImageBytes, "image/png"
            );
            // 3. Make API call
            IUserMobileApiService apiService = ApiClient.getClientWithToken(this).create(IUserMobileApiService.class);
            apiService.confirmDriver(request).enqueue(new Callback<RequestBody>() {
                @Override
                public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ConfirmDriverThreeActivity.this, "Driver confirmed successfully!", Toast.LENGTH_SHORT).show();
                        // You can navigate to another activity if needed
                    } else {
                        Toast.makeText(ConfirmDriverThreeActivity.this, "Failed to confirm driver: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RequestBody> call, Throwable t) {
                    Toast.makeText(ConfirmDriverThreeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
    private void loadImageWithFixHost(String url, ImageView target) {
        if (url != null && !url.isEmpty()) {
            if (url.contains("localhost")) {
                url = url.replace("http://localhosts", "http://10.0.2.2:8080");
            }

            Log.d("ImageDebug", "Image URL: " + url);

            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logout)
                    .into(target);
        }
    }
    private void openVehicleImageViewicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    private void openCarRegistrationImageViewPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_TWO_PICK);
    }
    private void openInspectionCertificateImageViewPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_THREE_PICK);
    }
    private void openInsuranceImageViewPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_FOUR_PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_PICK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                vehicleImageView.setImageURI(selectedImageUri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    vehicleImageBytes = stream.toByteArray();
                    Log.d("ImageBytes", "Byte array size: " + vehicleImageBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to process image!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_TWO_PICK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                carRegistrationImageView.setImageURI(selectedImageUri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    carRegistrationImageBytes = stream.toByteArray();
                    Log.d("ImageBytes", "Byte array size: " + carRegistrationImageBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to process image!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_THREE_PICK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                inspectionCertificateImageView.setImageURI(selectedImageUri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    inspectionCertificateImageBytes = stream.toByteArray();
                    Log.d("ImageBytes", "Byte array size: " + inspectionCertificateImageBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to process image!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_FOUR_PICK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                insuranceImageView.setImageURI(selectedImageUri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    insuranceImageBytes = stream.toByteArray();
                    Log.d("ImageBytes", "Byte array size: " + insuranceImageBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to process image!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



}
