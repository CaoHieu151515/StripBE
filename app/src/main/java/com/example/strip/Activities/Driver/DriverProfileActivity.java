package com.example.strip.Activities.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.strip.Activities.StripActivity;
import com.example.strip.Adapters.VehicleAdapter;
import com.example.strip.Models.DriverVehicleDTO;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.ErrorTranslate;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.network.ApiClient;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverProfileActivity extends AppCompatActivity {


    private TextView tvEmail, tvFullName, tvPhone, tvGender, tvAddress, tvDob, tvCountTrip, tvBannedDay, tvDriverStatus;
    private ImageView ivProfile;
    private RecyclerView rvVehicles;
    private VehicleAdapter vehicleAdapter;
    private List<DriverVehicleDTO> vehicleList = new ArrayList<>();
    private NotificationPopup notificationPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver); // Update with your actual XML file name
        ImageView btnBack = findViewById(R.id.backButton);
        tvCountTrip = findViewById(R.id.tvCountTrip);
        tvBannedDay = findViewById(R.id.tvBannedDay);
        tvDriverStatus = findViewById(R.id.tvDriverStatus);
        tvEmail = findViewById(R.id.tvEmail);
        tvFullName = findViewById(R.id.tvFullName);
        tvPhone = findViewById(R.id.tvPhone);
        tvGender = findViewById(R.id.tvGender);
        tvAddress = findViewById(R.id.tvAddress);
        tvDob = findViewById(R.id.tvDob);
        ivProfile = findViewById(R.id.ivProfile);
        rvVehicles = findViewById(R.id.rvVehicles);
        rvVehicles.setLayoutManager(new LinearLayoutManager(this));
        vehicleAdapter = new VehicleAdapter(vehicleList);
        rvVehicles.setAdapter(vehicleAdapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverProfileActivity.this, StripActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fetchDriverInfo();
    }
//    private Retrofit getRetrofitClient() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        String jwtToken = sharedPreferences.getString("jwtToken", null);
//        if (jwtToken == null) {
//            Toast.makeText(DriverProfileActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
//        }
//        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
//                .newBuilder()
//                .addInterceptor(chain -> {
//                    Request.Builder requestBuilder = chain.request().newBuilder();
//                    if (jwtToken != null) {
//                        requestBuilder.addHeader("Authorization", "Bearer " + jwtToken);
//                    }
//                    return chain.proceed(requestBuilder.build());
//                })
//                .build();
//
//
//        return new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/")
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }
    private void fetchDriverInfo() {
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService apiService = retrofit.create(IUserMobileApiService.class);
        Call<UserMoreResponse> call = apiService.getUserInfo();

        call.enqueue(new Callback<UserMoreResponse>() {
            @Override
            public void onResponse(Call<UserMoreResponse> call, Response<UserMoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserMoreResponse user = response.body();
                    tvEmail.setText(user.getUser().getEmail());
                    String fullName = (user.getUser().getFirstName() != null ? user.getUser().getFirstName() : "") +
                            (user.getUser().getLastName() != null ? " " + user.getUser().getLastName() : "");

                    if (fullName.trim().isEmpty()) {
                        fullName = "Chưa có thông tin";
                    }
                    tvFullName.setText(fullName);
                    tvPhone.setText(user.getUserDetailsCusDTO().getPhone() != null ? user.getUserDetailsCusDTO().getPhone() : "Chưa có thông tin");
                    tvGender.setText(user.getUserDetailsCusDTO().getGender() != null ? user.getUserDetailsCusDTO().getGender() : "Chưa có thông tin");
                    tvAddress.setText(user.getUserDetailsCusDTO().getAddress() != null ? user.getUserDetailsCusDTO().getAddress() : "Chưa có thông tin");
                    tvDob.setText(user.getUserDetailsCusDTO().getDob() != null ? user.getUserDetailsCusDTO().getDob() : "Chưa có thông tin");
                    int driverPoint = (user.getDriver() != null) ? user.getDriver().getDriverPoint() : -1;
                    tvCountTrip.setText(driverPoint >= 0 ? "" + driverPoint : "Chưa có thông tin");
                    tvBannedDay.setText(user.getDriver().getBannedDay() != null ? user.getDriver().getBannedDay() : "Chưa có thông tin");
                    tvDriverStatus.setText(user.getDriver().getDriverStatus() != null ? user.getDriver().getDriverStatus() : "Chưa có thông tin");
                    // Hiển thị ảnh nếu có
                    String imageUrl = user.getUserDetailsCusDTO().getImageUrl();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        if (imageUrl.contains("localhost")) {
                            imageUrl = imageUrl.replace("https://localhost", "http://10.0.2.2:8080");
                        }
                        if (!DriverProfileActivity.this.isFinishing()
                                && !DriverProfileActivity.this.isDestroyed()) {
                            Glide.with(DriverProfileActivity.this)
                                    .load(imageUrl)
                                    .into(ivProfile);
                        }
                    }
                    // Update Vehicle List
                    if (user.getDriverVehicleDTO() != null) {
                        vehicleList.clear();
                        vehicleList.addAll(user.getDriverVehicleDTO());
                        vehicleAdapter.notifyDataSetChanged();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Thất bại khi tham gia chuyến đi! " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Thất bại khi cập nhật hồ sơ! \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Thất bại khi cập nhật hồ sơ! \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserMoreResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }

}
