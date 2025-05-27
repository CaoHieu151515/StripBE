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
import com.example.strip.Activities.Driver.WalletActivity;
import com.example.strip.Activities.Wallet.PaymentActivity;
import com.example.strip.Adapters.TransactionAdapter;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.Models.Response.WalletResponse;
import com.example.strip.Models.Transaction;
import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.DateFormatter;
import com.example.strip.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatFragment extends Fragment {

    private TextView tvMobifyDate, tvCurrent, tvBefore, tvAmount;
    private ImageView ivProfile,ivPayment;
    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        tvMobifyDate = view.findViewById(R.id.tvMobifyDate);
        tvCurrent = view.findViewById(R.id.tvCurrent);
        tvBefore = view.findViewById(R.id.tvBefore);
        tvAmount = view.findViewById(R.id.tvAmount);
        ivProfile = view.findViewById(R.id.profileImage);
        ivPayment = view.findViewById(R.id.ivPayment);
        ivPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                startActivity(intent);
            }
        });

        fetchWalletInfo();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchTransactionsList();
        // Inflate the layout for this fragment
        return view;
    }
    private void fetchWalletInfo() {
        Retrofit retrofit = ApiClient.getClientWithToken(getContext());
        IUserMobileApiService apiService = retrofit.create(IUserMobileApiService.class);
        Call<UserMoreResponse> call = apiService.getUserInfo();

        call.enqueue(new Callback<UserMoreResponse>() {
            @Override
            public void onResponse(Call<UserMoreResponse> call, Response<UserMoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserMoreResponse user = response.body();
                    tvMobifyDate.setText(user.getUserWallet().getMobifyDate() != null ? DateFormatter.formatDate(user.getUserWallet().getMobifyDate()) : "");
                    tvCurrent.setText(String.format("%.2f", user.getUserWallet().getCurrent()));
                    tvBefore.setText(String.format("%.2f", user.getUserWallet().getBefore()));
                    tvAmount.setText(String.format("%.2f", user.getUserWallet().getAmount()));
                    String imageUrl = user.getUserDetailsCusDTO().getImageUrl();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        if (imageUrl.contains("localhost")) {
                            imageUrl = imageUrl.replace("https://localhost", "http://10.0.2.2:8080");
                        }
                        if (isAdded() && ivProfile != null && imageUrl != null && !imageUrl.isEmpty()) {
                            if (imageUrl.contains("localhost")) {
                                imageUrl = imageUrl.replace("https://localhost", "http://10.0.2.2:8080");
                            }
                            Glide.with(requireContext())
                                    .load(imageUrl)
                                    .into(ivProfile);
                        }

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
    private void fetchTransactionsList() {
        Retrofit retrofit = ApiClient.getClientWithToken(getContext());
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
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "API request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}