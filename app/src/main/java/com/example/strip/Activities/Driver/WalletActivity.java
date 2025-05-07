package com.example.strip.Activities.Driver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.strip.Activities.Trip.TripFindActivity;
import com.example.strip.Activities.Trip.TripPublishActivity;
//import com.example.strip.Activities.Wallet.PaymentActivity;
import com.example.strip.Activities.Wallet.PaymentActivity;
import com.example.strip.Adapters.TransactionAdapter;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.Models.Response.WalletResponse;
import com.example.strip.Models.Transaction;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.DateFormatter;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WalletActivity extends AppCompatActivity {

    private TextView tvUserWallet, tvMobifyDate, tvCurrent, tvBefore, tvAmount;
    private ImageView ivProfile,ivPayment;
    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet); // Update with your actual XML file name

        tvUserWallet = findViewById(R.id.tvUserWallet);
        tvMobifyDate = findViewById(R.id.tvMobifyDate);
        tvCurrent = findViewById(R.id.tvCurrent);
        tvBefore = findViewById(R.id.tvBefore);
        tvAmount = findViewById(R.id.tvAmount);
        ivProfile = findViewById(R.id.profileImage);
        ivPayment = findViewById(R.id.ivPayment);
        ivPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WalletActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });
        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fetchWalletInfo();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchTransactionsList();
    }
//    private Retrofit getRetrofitClient() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        String jwtToken = sharedPreferences.getString("jwtToken", null);
//        if (jwtToken == null) {
//            Toast.makeText(WalletActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
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
//        return new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/")
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }
    private void fetchWalletInfo() {
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService apiService = retrofit.create(IUserMobileApiService.class);
        Call<UserMoreResponse> call = apiService.getUserInfo();

        call.enqueue(new Callback<UserMoreResponse>() {
            @Override
            public void onResponse(Call<UserMoreResponse> call, Response<UserMoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserMoreResponse user = response.body();
                    tvUserWallet.setText(user.getUserWallet().getUserWallet() != null ? user.getUserWallet().getUserWallet() : "N/A");
                    tvMobifyDate.setText(user.getUserWallet().getMobifyDate() != null ? DateFormatter.formatDate(user.getUserWallet().getMobifyDate()) : "N/A");
                    tvCurrent.setText(String.format("%.2f", user.getUserWallet().getCurrent()));
                    tvBefore.setText(String.format("%.2f", user.getUserWallet().getBefore()));
                    tvAmount.setText(String.format("%.2f", user.getUserWallet().getAmount()));
                    String imageUrl = user.getUserDetailsCusDTO().getImageUrl();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        if (imageUrl.contains("localhost")) {
                            imageUrl = imageUrl.replace("https://localhost", "http://10.0.2.2:8080");
                        }
                        Glide.with(WalletActivity.this)
                                .load(imageUrl)
                                .into(ivProfile);
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Error", "Lỗi khi lấy thông tin: " + errorBody);
                        Toast.makeText(WalletActivity.this, "Lỗi khi lấy thông tin: " + response.code(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(WalletActivity.this, "Lỗi khi lấy thông tin: Không thể lấy thông báo lỗi" + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserMoreResponse> call, Throwable t) {
                Log.e("Error", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(WalletActivity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchTransactionsList() {
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService apiService = retrofit.create(IUserMobileApiService.class);
        Call<WalletResponse> call = apiService.getWalletDetails();
        call.enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Transaction> transactions = response.body().getTransactions();
                    transactionAdapter = new TransactionAdapter(transactions);
                    recyclerView.setAdapter(transactionAdapter);
                } else {
                    Toast.makeText(WalletActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(WalletActivity.this, "API request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
