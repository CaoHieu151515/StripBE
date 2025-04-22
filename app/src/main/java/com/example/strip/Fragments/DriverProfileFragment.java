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
import com.example.strip.Activities.Driver.AddTripActivity;
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
    private TextView tvEmail, tvFullName, tvPhone, tvGender, tvAddress, tvDob, tvCountTrip, tvBannedDay, tvDriverStatus;
    private ImageView ivProfile, ivChangeToPassenger;
    private RecyclerView rvVehicles;
    private VehicleAdapter vehicleAdapter;
    private List<DriverVehicleDTO> vehicleList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_profile, container, false);
        ImageView btnBack = view.findViewById(R.id.backButton);
        tvCountTrip = view.findViewById(R.id.tvCountTrip);
        tvBannedDay = view.findViewById(R.id.tvBannedDay);
        tvDriverStatus = view.findViewById(R.id.tvDriverStatus);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvGender = view.findViewById(R.id.tvGender);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvDob = view.findViewById(R.id.tvDob);
        ivProfile = view.findViewById(R.id.ivProfile);
        rvVehicles = view.findViewById(R.id.rvVehicles);
        ivChangeToPassenger = view.findViewById(R.id.ivChangeToPassenger);
        rvVehicles.setLayoutManager(new LinearLayoutManager(getContext()));
        vehicleAdapter = new VehicleAdapter(vehicleList);
        rvVehicles.setAdapter(vehicleAdapter);
        ivChangeToPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StripActivity.class);
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
                        fullName = "N/A";
                    }
                    tvFullName.setText(fullName);
                    tvPhone.setText(user.getUserDetailsCusDTO().getPhone() != null ? user.getUserDetailsCusDTO().getPhone() : "N/A");
                    tvGender.setText(user.getUserDetailsCusDTO().getGender() != null ? user.getUserDetailsCusDTO().getGender() : "N/A");
                    tvAddress.setText(user.getUserDetailsCusDTO().getAddress() != null ? user.getUserDetailsCusDTO().getAddress() : "N/A");
                    tvDob.setText(user.getUserDetailsCusDTO().getDob() != null ? user.getUserDetailsCusDTO().getDob() : "N/A");
                    int driverPoint = (user.getDriver() != null) ? user.getDriver().getDriverPoint() : -1;
                    tvCountTrip.setText(driverPoint >= 0 ? "" + driverPoint : "N/A");
                    tvBannedDay.setText(user.getDriver().getBannedDay() != null ? user.getDriver().getBannedDay() : "N/A");
                    tvDriverStatus.setText(user.getDriver().getDriverStatus() != null ? user.getDriver().getDriverStatus() : "N/A");
                    // Hiển thị ảnh nếu có
                    String imageUrl = user.getUserDetailsCusDTO().getImageUrl();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        if (imageUrl.contains("localhost")) {
                            imageUrl = imageUrl.replace("https://localhost:8080", "http://10.0.2.2:8080");
                        }
                        Glide.with(getContext())
                                .load(imageUrl)
                                .into(ivProfile);
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