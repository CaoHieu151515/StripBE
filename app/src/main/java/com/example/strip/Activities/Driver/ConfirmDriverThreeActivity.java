package com.example.strip.Activities.Driver;

import android.app.AlertDialog;
import android.content.Intent;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.strip.Models.Request.ConfirmDriverRequest;
import com.example.strip.Models.Request.NotificationRequest;
import com.example.strip.Models.Response.ConfirmDriverResponse;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.Models.Response.VehicleResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.ErrorTranslate;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.network.ApiClient;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    private NotificationPopup notificationPopup;
    private UserMoreResponse user;
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
        notificationPopup = new NotificationPopup(this);
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
        fetchUserInfo();
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService userService = retrofit.create(IUserMobileApiService.class);

        Call<ConfirmDriverResponse> call = userService.getConfirmDriver();

        call.enqueue(new Callback<ConfirmDriverResponse>() {
            @Override
            public void onResponse(Call<ConfirmDriverResponse> call, Response<ConfirmDriverResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ConfirmDriverResponse data = response.body();
                    VehicleResponse vehicle = data.getVehicleResponse();

                    String vehicleType = "";
                    String vehicleColor = "";
                    String vehicleNumber = "";
                    String vehicleBrand = "";
                    String numberOfSeats = "0";

                    if (vehicle != null) {
                        if (vehicle.getVehicleType() != null) vehicleType = vehicle.getVehicleType();
                        if (vehicle.getVehicleColor() != null) vehicleColor = vehicle.getVehicleColor();
                        if (vehicle.getVehicleNumber() != null) vehicleNumber = vehicle.getVehicleNumber();
                        if (vehicle.getVehicleBrand() != null) vehicleBrand = vehicle.getVehicleBrand();
                        numberOfSeats = String.valueOf(vehicle.getNumberOfSeats());

                        // Load images safely
                        loadImageWithFixHost(vehicle.getVehicleImageUrl(), vehicleImageView);
                        loadImageWithFixHost(vehicle.getCarregistrationUrl(), carRegistrationImageView);
                        loadImageWithFixHost(vehicle.getVehicleInspectionCertificateUrl(), inspectionCertificateImageView);
                        loadImageWithFixHost(vehicle.getCarInsuranceUrl(), insuranceImageView);
                    }

                    // Update UI fields
                    etVehicleType.setText(vehicleType);
                    etVehicleColor.setText(vehicleColor);
                    etVehicleNumber.setText(vehicleNumber);
                    etSeats.setText(numberOfSeats);
                    etVehicleBrand.setText(vehicleBrand);
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
            String userId = user.getDriver().getUserId();

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
            apiService.confirmDriver(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.e("Success", "Xác nhận tài xế thành công! " + response.body());
                        NotificationRequest notiRequest = new NotificationRequest(
                                "Bạn đã xác nhận tài xế!", // title or message
                                "Bạn đã xác nhận tài xế vui lòng chờ kết quả! ", // detailed message
                                userId // or other target
                        );
                        notificationPopup.showPopup("Xác nhận tài xế thành công!", false);
                        notificationPopup.createNotification(notiRequest);
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            finish();
                        }, 1500);
                        // You can navigate to another activity if needed
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("Failed", "Không thể gửi xác nhận tài xế. " + errorBody);

                            JSONObject jsonObject = new JSONObject(errorBody);

                            String errorMessageCode = "";

                            // Trường hợp 1: properties là JSONObject
                            if (jsonObject.has("properties")) {
                                Object props = jsonObject.get("properties");

                                if (props instanceof JSONObject) {
                                    errorMessageCode = ((JSONObject) props).optString("message", "");
                                } else if (props instanceof String) {
                                    // Trường hợp 2: properties là chuỗi như {message=error.already-exists, params=feedback}
                                    String propsStr = (String) props;
                                    // Chuyển sang dạng JSON hợp lệ
                                    propsStr = propsStr.replace("=", "\":\"").replace(", ", "\", \"").replace("{", "{\"").replace("}", "\"}");

                                    try {
                                        JSONObject propsJson = new JSONObject(propsStr);
                                        errorMessageCode = propsJson.optString("message", "");
                                    } catch (Exception e) {
                                        errorMessageCode = "Lỗi không xác định";
                                    }
                                }
                            }

                            // Fallback nếu không có message
                            if (errorMessageCode.isEmpty()) {
                                errorMessageCode = jsonObject.optString("message", "Lỗi không xác định");
                            }

                            // Dịch lỗi
                            String translated = ErrorTranslate.translateError(errorMessageCode);
                            notificationPopup.showPopup("Không thể gửi xác nhận tài xế.\n" + translated, true);

                        } catch (Exception e) {
                            e.printStackTrace();
                            notificationPopup.showPopup("Không thể gửi xác nhận tài xế.\nKhông thể lấy thông báo lỗi", true);
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
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

            if (!ConfirmDriverThreeActivity.this.isFinishing()
                    && !ConfirmDriverThreeActivity.this.isDestroyed()) {
                Glide.with(this)
                        .load(url)
                        .skipMemoryCache(true) // Skip memory cache
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip disk cache
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logout)
                        .into(target);
            }
            Log.d("ImageDebug", "Image URL: " + url);

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
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    insuranceImageBytes = stream.toByteArray();
                    Log.d("ImageBytes", "Byte array size: " + insuranceImageBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to process image!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void fetchUserInfo() {
        Retrofit retrofit = ApiClient.getClientWithToken(ConfirmDriverThreeActivity.this);
        IUserMobileApiService apiService = retrofit.create(IUserMobileApiService.class);
        Call<UserMoreResponse> call = apiService.getUserInfo();

        call.enqueue(new Callback<UserMoreResponse>() {
            @Override
            public void onResponse(Call<UserMoreResponse> call, Response<UserMoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                } else {
                    try {
                        String errorBody = response.errorBody().string();

                        Log.e("Error", "Lỗi khi lấy thông tin: " + errorBody);
                        Toast.makeText(ConfirmDriverThreeActivity.this, "Lỗi khi lấy thông tin: " + response.code(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Lỗi khi lấy thông tin: \nKhông thể lấy thông báo lỗi\n" + response.code(), true);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserMoreResponse> call, Throwable t) {
                Log.e("Error", "Lỗi khi gọi API: " + t.getMessage(), t);
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }


}
