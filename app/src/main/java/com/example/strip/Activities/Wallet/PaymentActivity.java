package com.example.strip.Activities.Wallet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.BraintreeClient;
import com.braintreepayments.api.PayPalAccountNonce;
import com.braintreepayments.api.PayPalCheckoutRequest;
import com.braintreepayments.api.PayPalClient;


import com.braintreepayments.api.PayPalListener;
import com.example.strip.R;
import com.example.strip.Services.IBrainTreeApiService;
import com.example.strip.Utils.UnsafeOkHttpClient;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class PaymentActivity extends AppCompatActivity {

    private Button btnPay;
    private TextView tvResult;
    private String clientToken;
    private PayPalClient payPalClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnPay = findViewById(R.id.btnPay);
        tvResult = findViewById(R.id.tvResult);


        // Load client token
        fetchClientToken();

        // Pay button click
        btnPay.setOnClickListener(v -> {
            if (clientToken != null) {
                BraintreeClient braintreeClient = new BraintreeClient(PaymentActivity.this, clientToken);
                payPalClient = new PayPalClient(PaymentActivity.this, braintreeClient);

                // Register the PayPalListener before calling tokenize
                payPalClient.setListener(new PayPalListener() {
                    @Override
                    public void onPayPalSuccess(@NonNull PayPalAccountNonce payPalAccountNonce) {
                        String nonce = payPalAccountNonce.getString();
                        Log.d("NONCE", "Received nonce: " + nonce);
                        tvResult.setText("Payment Success! Nonce: " + nonce);
                        // TODO: send nonce to your backend for transaction
                    }

                    @Override
                    public void onPayPalFailure(@NonNull Exception error) {
                        Log.e("PAYPAL", "Payment failed: " + error.getMessage());
                        tvResult.setText("Payment failed: " + error.getMessage());
                    }
                });

                // Create the request and start the PayPal flow
                PayPalCheckoutRequest request = new PayPalCheckoutRequest("10.00");
                request.setCurrencyCode("USD");

                payPalClient.tokenizePayPalAccount(PaymentActivity.this, request);
            } else {
                Toast.makeText(this, "Client token not ready yet", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void fetchClientToken() {
        Retrofit retrofit = getRetrofitClient();
        IBrainTreeApiService service = retrofit.create(IBrainTreeApiService.class);
        Call<ResponseBody> call = service.getClientToken();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        clientToken = response.body().string();
                        Log.d("TOKEN", "Client Token: " + clientToken);
                        tvResult.setText("Token ready. You can now press Pay.");
                    } catch (IOException e) {
                        e.printStackTrace();
                        tvResult.setText("Error parsing token");
                    }
                } else {
                    tvResult.setText("Failed to get client token");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                tvResult.setText("API Error: " + t.getMessage());
            }
        });
    }

    private Retrofit getRetrofitClient() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken == null) {
            Toast.makeText(PaymentActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
        }

        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                .newBuilder()
                .addInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    if (jwtToken != null) {
                        requestBuilder.addHeader("Authorization", "Bearer " + jwtToken);
                    }
                    return chain.proceed(requestBuilder.build());
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
