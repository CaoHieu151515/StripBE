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
import com.example.strip.Activities.Driver.AddTripActivity;
import com.example.strip.Activities.Driver.AddVehicleActivity;
import com.example.strip.Activities.Driver.DriverProfileActivity;
import com.example.strip.Activities.StripActivity;
import com.example.strip.Adapters.VehicleAdapter;
import com.example.strip.Models.DriverVehicleDTO;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverProfileFragment extends Fragment {
    private TextView tvEmail, tvFullName, tvPhone, tvGender, tvAddress, tvDob;
    private ImageView ivProfile, ivChangeToPassenger, ivVehicleIcon;
    private RecyclerView rvVehicles;
    private VehicleAdapter vehicleAdapter;
    private List<DriverVehicleDTO> vehicleList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_profile, container, false);
        ImageView btnBack = view.findViewById(R.id.backButton);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvGender = view.findViewById(R.id.tvGender);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvDob = view.findViewById(R.id.tvDob);
        ivProfile = view.findViewById(R.id.ivProfile);
        rvVehicles = view.findViewById(R.id.rvVehicles);
        ivVehicleIcon = view.findViewById(R.id.ivVehicleIcon);
        ivChangeToPassenger = view.findViewById(R.id.ivChangeToPassenger);
        rvVehicles.setLayoutManager(new LinearLayoutManager(getContext()));
        vehicleAdapter = new VehicleAdapter(vehicleList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvVehicles.setLayoutManager(layoutManager);
        rvVehicles.setAdapter(vehicleAdapter);
        ivChangeToPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StripActivity.class);
                startActivity(intent);
            }
        });
        ivVehicleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddVehicleActivity.class);
                startActivity(intent);
            }
        });
        fetchDriverInfo();
        // Inflate the layout for this fragment
        return view;
    }
    private void fetchDriverInfo() {
        Retrofit retrofit = ApiClient.getClientWithToken(getContext());
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
                        fullName = "";
                    }
                    tvFullName.setText(fullName);
                    tvPhone.setText(user.getUserDetailsCusDTO().getPhone() != null ? user.getUserDetailsCusDTO().getPhone() : "");
                    tvGender.setText(user.getUserDetailsCusDTO().getGender() != null ? user.getUserDetailsCusDTO().getGender() : "");
                    tvAddress.setText(user.getUserDetailsCusDTO().getAddress() != null ? user.getUserDetailsCusDTO().getAddress() : "");
                    tvDob.setText(user.getUserDetailsCusDTO().getDob() != null ? user.getUserDetailsCusDTO().getDob() : "");
                    // Hiển thị ảnh nếu có
                    String imageUrl = user.getUserDetailsCusDTO().getImageUrl();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        if (imageUrl.contains("localhost")) {
                            imageUrl = imageUrl.replace("https://localhost:8080", "http://10.0.2.2:8080");
                        }
                        if (isAdded() && getContext() != null) {
                            Glide.with(getContext())
                                    .load(imageUrl)
                                    .into(ivProfile);
                            // an toàn để update UI hoặc dùng Glide
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
}