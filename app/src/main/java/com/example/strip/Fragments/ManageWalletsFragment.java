package com.example.strip.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.strip.Activities.Driver.WalletActivity;
import com.example.strip.Activities.Wallet.PaymentActivity;
import com.example.strip.Adapters.RatingAdapter;
import com.example.strip.Adapters.TransactionAdapter;
import com.example.strip.Models.Response.DriverInfoResponse;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.Models.Response.WalletResponse;
import com.example.strip.Models.Transaction;
import com.example.strip.R;
import com.example.strip.Services.ITripMobileApiService;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.DateFormatter;
import com.example.strip.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ManageWalletsFragment extends Fragment {
    private ImageView imgAvatar;
    private TextView txtName, txtPhone, txtEmail, txtAddress, txtRating;
    private RecyclerView rvRatings;
    private UserMoreResponse user;
    private String driverId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_wallets, container, false);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtName = view.findViewById(R.id.txtName);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtRating = view.findViewById(R.id.txtRating);
        rvRatings = view.findViewById(R.id.rvRatings);

        rvRatings.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchUserInfo();
        fetchDriverWithRating(driverId);
        return view;
    }
    private void fetchUserInfo() {
        Retrofit retrofit = ApiClient.getClientWithToken(requireContext());
        IUserMobileApiService apiService = retrofit.create(IUserMobileApiService.class);
        Call<UserMoreResponse> call = apiService.getUserInfo();

        call.enqueue(new Callback<UserMoreResponse>() {
            @Override
            public void onResponse(Call<UserMoreResponse> call, Response<UserMoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    driverId = user.getDriver().getDriverID();

                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Error", "Lỗi khi lấy thông tin: " + errorBody);
                        Toast.makeText(getContext(), "Lỗi khi lấy thông tin: " + response.code(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Lỗi khi lấy thông tin: Không thể lấy thông báo lỗi" + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserMoreResponse> call, Throwable t) {
                Log.e("Error", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchDriverWithRating(String driverId) {
        ITripMobileApiService api = ApiClient.getClientWithToken(getContext()).create(ITripMobileApiService.class);
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
                            Glide.with(getContext()).load(driver.avatar).into(imgAvatar);

                            RatingAdapter adapter = new RatingAdapter(driver.ratingOfDriverInfoResponseList);
                            rvRatings.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<DriverInfoResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}