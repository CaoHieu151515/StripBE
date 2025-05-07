package com.example.strip.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.strip.Activities.Account.BeginActivity;
import com.example.strip.Activities.Customer.ChangePasswordActivity;
import com.example.strip.Activities.Customer.ConfirmDriverActivity;
import com.example.strip.Activities.Customer.EditProfilePassengerActivity;
import com.example.strip.Activities.Customer.ViewPackagesActivity;
import com.example.strip.Activities.Driver.ConfirmDriverOneActivity;
import com.example.strip.Activities.StripActivity;
import com.example.strip.Activities.StripDriverActivity;
import com.example.strip.Activities.Wallet.PaymentActivity;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.DateFormatter;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import im.crisp.client.external.Crisp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountFragment extends Fragment {

    private TextView tvLogin, tvEmail, tvFullName, tvPhone, tvGender, tvAddress, tvDob;
    private ImageView ivProfile, ivChangePassword, ivUpdateToDriver, ivConfirmDriver, ivChangeToDriver, ivLogout, ivWallet;
    private Button btnEditProfile;
    private UserMoreResponse user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Ánh xạ UI
        tvLogin = view.findViewById(R.id.tvLogin);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvGender = view.findViewById(R.id.tvGender);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvDob = view.findViewById(R.id.tvDob);
        ivProfile = view.findViewById(R.id.ivProfile);
        ivConfirmDriver = view.findViewById(R.id.ivConfirmDriver);
        ivChangePassword = view.findViewById(R.id.ivChangePassword);
        ivUpdateToDriver = view.findViewById(R.id.ivUpdateToDriver);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        ivChangeToDriver = view.findViewById(R.id.ivChangeToDriver);
        ivLogout = view.findViewById(R.id.ivLogout);
        ivWallet = view.findViewById(R.id.ivWallet);
        ivWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                startActivity(intent);
            }
        });
        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogout();
            }
        });
        ivChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });
        ivUpdateToDriver.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ViewPackagesActivity.class);
            startActivity(intent);
        });
        ivConfirmDriver.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ConfirmDriverOneActivity.class);
            startActivity(intent);
        });
        ivChangeToDriver.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StripDriverActivity.class);
            startActivity(intent);
        });
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfilePassengerActivity.class);
            intent.putExtra("imageUrl", user.getUserDetailsCusDTO().getImageUrl());
            intent.putExtra("firstName", user.getUser().getFirstName());
            intent.putExtra("lastName", user.getUser().getLastName());
            intent.putExtra("phone", user.getUserDetailsCusDTO().getPhone());
            intent.putExtra("address", user.getUserDetailsCusDTO().getAddress());
            intent.putExtra("dob", user.getUserDetailsCusDTO().getDob());
            intent.putExtra("gender", user.getUserDetailsCusDTO().getGender());
            startActivity(intent);
        });
        // Gọi API để lấy dữ liệu
        fetchUserInfo();

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
                    tvLogin.setText(user.getUser().getLogin());
                    tvEmail.setText(user.getUser().getEmail());
                    String fullName = (user.getUser().getFirstName() != null ? user.getUser().getFirstName() : "") +
                            (user.getUser().getLastName() != null ? " " + user.getUser().getLastName() : "");

                    if (fullName.trim().isEmpty()) {
                        fullName = "N/A";
                    }
                    tvFullName.setText(fullName);
                    tvPhone.setText(user.getUserDetailsCusDTO().getPhone() != null ? user.getUserDetailsCusDTO().getPhone() : "N/A");
                    tvGender.setText(user.getUserDetailsCusDTO().getGender() != null ? user.getUserDetailsCusDTO().getGender() : "N/A");
                    tvAddress.setText(user.getUserDetailsCusDTO().getAddress() != null ? user.getUserDetailsCusDTO().getAddress() : "N/A");
                    tvDob.setText(user.getUserDetailsCusDTO().getDob() != null ? DateFormatter.formatDatePrimary(user.getUserDetailsCusDTO().getDob()) : "N/A");
                    // Hiển thị ảnh nếu có
                    String ivImageUrl = user.getUserDetailsCusDTO().getImageUrl();
                    if (ivImageUrl != null && !ivImageUrl.isEmpty()) {
                        if (ivImageUrl.contains("localhost")) {
                            ivImageUrl = ivImageUrl.replace("https://localhost:8080", "http://10.0.2.2:8080");
                        }
                        Log.d("ImageDebug", "URL=[" + ivImageUrl + "]");

                        Glide.with(requireContext()) // or getContext(), depending on where this is
                                .load(ivImageUrl.trim())
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logout)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(ivProfile);

                        Log.e("ImageLoadError", "Failed to load image from: [" + ivImageUrl + "]");
                    }

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
    public void handleLogout() {
        // Clear chat data for the current user
        Crisp.resetChatSession(getContext().getApplicationContext());

        // Continue with logout logic, like clearing user data and redirecting to login
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("jwtToken"); // Clear any saved user authentication data
        editor.apply();

        Intent intent = new Intent(getContext(), BeginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}