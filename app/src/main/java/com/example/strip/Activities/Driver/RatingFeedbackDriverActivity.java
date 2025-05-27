package com.example.strip.Activities.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.strip.Activities.Wallet.PaymentActivity;
import com.example.strip.Adapters.RatingAdapter;
import com.example.strip.Models.Response.DriverInfoResponse;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RatingFeedbackDriverActivity extends AppCompatActivity {
    private ImageView imgAvatar;
    private TextView txtName, txtPhone, txtEmail, txtAddress, txtRating;
    private RecyclerView rvRatings;
    private UserMoreResponse user;
    private String driverId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_feedback_driver); // Update with your actual XML file name
        imgAvatar = findViewById(R.id.imgAvatar);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtAddress = findViewById(R.id.txtAddress);
        txtRating = findViewById(R.id.txtRating);
        rvRatings = findViewById(R.id.rvRatings);

        rvRatings.setLayoutManager(new LinearLayoutManager(this));
        driverId = getIntent().getStringExtra("driverId");
        if (driverId == null) {
            Toast.makeText(this, "Invalid Driver ID!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fetchUserInfo();
    }
    private void fetchUserInfo() {
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService apiService = retrofit.create(IUserMobileApiService.class);
        Call<UserMoreResponse> call = apiService.getUserInfo();

        call.enqueue(new Callback<UserMoreResponse>() {
            @Override
            public void onResponse(Call<UserMoreResponse> call, Response<UserMoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    driverId = user.getDriver().getDriverID().toString();
                    fetchDriverWithRating(driverId);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Error", "Lỗi khi lấy thông tin: " + errorBody);
                        Toast.makeText(RatingFeedbackDriverActivity.this, "Lỗi khi lấy thông tin: " + response.code(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(RatingFeedbackDriverActivity.this, "Lỗi khi lấy thông tin: Không thể lấy thông báo lỗi" + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserMoreResponse> call, Throwable t) {
                Log.e("Error", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(RatingFeedbackDriverActivity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchDriverWithRating(String driverId) {
        ITripMobileApiService api = ApiClient.getClientWithToken(RatingFeedbackDriverActivity.this).create(ITripMobileApiService.class);
        api.getDriverInfo(driverId)
                .enqueue(new Callback<DriverInfoResponse>() {
                    @Override
                    public void onResponse(Call<DriverInfoResponse> call, Response<DriverInfoResponse> response) {
                        if (response.isSuccessful()) {
                            DriverInfoResponse driver = response.body();
                            txtName.setText("Họ và tên: "+ driver.firstName + " " + driver.lastName);
                            txtPhone.setText("Số điện thoại: "+driver.phone);
                            txtEmail.setText("Email: "+driver.email);
                            txtAddress.setText("Địa chỉ: "+driver.address);
                            txtRating.setText("Xếp hạng: " + driver.averageRating);
                            Glide.with(RatingFeedbackDriverActivity.this).load(driver.avatar).into(imgAvatar);

                            RatingAdapter adapter = new RatingAdapter(driver.ratingOfDriverInfoResponseList);
                            rvRatings.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<DriverInfoResponse> call, Throwable t) {
                        Toast.makeText(RatingFeedbackDriverActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
