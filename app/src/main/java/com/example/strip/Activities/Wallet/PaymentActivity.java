package com.example.strip.Activities.Wallet;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.BraintreeClient;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.CardClient;
import com.braintreepayments.api.CardNonce;
import com.braintreepayments.api.ClientTokenCallback;
import com.braintreepayments.api.ClientTokenProvider;
import com.braintreepayments.api.DropInClient;
import com.braintreepayments.api.DropInListener;
import com.braintreepayments.api.DropInRequest;
import com.braintreepayments.api.DropInResult;
import com.braintreepayments.api.PayPalClient;
import com.braintreepayments.api.UserCanceledException;



import com.example.strip.Models.Request.PaymentRequest;
import com.example.strip.R;
import com.example.strip.Services.IBrainTreeApiService;
import com.example.strip.network.ApiClient;

import java.io.IOException;
import java.math.BigDecimal;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PaymentActivity extends AppCompatActivity implements DropInListener {

    private EditText etAmount;
    private TextView tvResult;
    private Button btnPay;

    private DropInClient dropInClient;
    private String clientToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        etAmount = findViewById(R.id.etAmount);
        tvResult = findViewById(R.id.tvResult);
        btnPay = findViewById(R.id.btnPay);

        // ✅ Tạo DropInClient SỚM từ onCreate bằng ClientTokenProvider
        dropInClient = new DropInClient(this, callback -> fetchClientToken(callback));
        dropInClient.setListener(this);


        btnPay.setOnClickListener(v -> {
            String amount = etAmount.getText().toString().trim();
            if (amount.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Mở giao diện Drop-In
            DropInRequest dropInRequest = new DropInRequest();
            dropInClient.launchDropIn(dropInRequest);
        });
    }

    private void fetchClientToken(ClientTokenCallback callback) {
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IBrainTreeApiService service = retrofit.create(IBrainTreeApiService.class);

        service.getClientToken().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        clientToken = response.body().string().trim();
                        callback.onSuccess(clientToken);
                    } catch (IOException e) {
                        callback.onFailure(e);
                    }
                } else {
                    callback.onFailure(new Exception("Không lấy được token"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    @Override
    public void onDropInSuccess(@NonNull DropInResult result) {
        if (result.getPaymentMethodNonce() != null) {
            String nonce = result.getPaymentMethodNonce().getString();
            String amount = etAmount.getText().toString().trim();
            Log.d("DROPIN", "✅ Nonce: " + nonce);
            sendNonceToBackend(amount, nonce);
        } else {
            Toast.makeText(this, "Không có phương thức thanh toán nào được chọn", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDropInFailure(@NonNull Exception error) {
        if (error instanceof UserCanceledException) {
            Toast.makeText(this, "🚫 Giao dịch bị huỷ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "❌ Lỗi Drop-In: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendNonceToBackend(String amount, String nonce) {
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IBrainTreeApiService service = retrofit.create(IBrainTreeApiService.class);

        service.checkout(nonce, amount).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PaymentActivity.this, "✅ Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                    tvResult.setText("Thành công");
                } else {
                    Toast.makeText(PaymentActivity.this, "❌ Backend lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "❌ Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
