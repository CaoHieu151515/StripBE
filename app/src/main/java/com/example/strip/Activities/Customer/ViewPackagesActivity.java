package com.example.strip.Activities.Customer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.strip.Adapters.PackageAdapter;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.NotificationPopup;
import java.util.List;
import com.example.strip.Models.PackageDriver;
import com.example.strip.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewPackagesActivity extends AppCompatActivity implements PackageAdapter.OnPackagePurchaseListener{
    private RecyclerView recyclerView;
    private PackageAdapter packageAdapter;
    private NotificationPopup notificationPopup;
    @Override
    public void onPackagePurchased() {
        notificationPopup.showPopup("Mua gói thành công!",false);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            finish();
        }, 1500);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_packages);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationPopup = new NotificationPopup(this);
        ImageView btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fetchPackages();
    }
//    private Retrofit getRetrofitClient() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        String jwtToken = sharedPreferences.getString("jwtToken", null);
//        if (jwtToken == null) {
//            Toast.makeText(ViewPackagesActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
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
    private void fetchPackages() {
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IUserMobileApiService apiService = retrofit.create(IUserMobileApiService.class);
        Call<List<PackageDriver>> call = apiService.getAllPackages();

        call.enqueue(new Callback<List<PackageDriver>>() {
            @Override
            public void onResponse(Call<List<PackageDriver>> call, Response<List<PackageDriver>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    packageAdapter = new PackageAdapter(ViewPackagesActivity.this,response.body(),ViewPackagesActivity.this);
                    recyclerView.setAdapter(packageAdapter);
                } else {
                    Toast.makeText(ViewPackagesActivity.this, "Failed to fetch packages", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PackageDriver>> call, Throwable t) {
                Log.e("API Error", "Error: " + t.getMessage(), t);
                Toast.makeText(ViewPackagesActivity.this, "API Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
