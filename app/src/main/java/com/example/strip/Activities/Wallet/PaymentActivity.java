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

    private EditText etAmount, etCardNumber, etExpiry, etCVV;
    private Button btnCardPay;
    private TextView tvResult;

    private String clientToken;
    private BraintreeClient braintreeClient;
    private CardClient cardClient;

    private DropInClient dropInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        etAmount = findViewById(R.id.etAmount);
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiry = findViewById(R.id.etExpiry);
        etCVV = findViewById(R.id.etCVV);
        btnCardPay = findViewById(R.id.btnCardPay);
        tvResult = findViewById(R.id.tvResult);

        dropInClient = new DropInClient(this, new ClientTokenProvider() {
            @Override
            public void getClientToken(@NonNull ClientTokenCallback callback) {
                fetchClientToken(callback);
            }
        });
        dropInClient.setListener(this);


        btnCardPay.setOnClickListener(v -> {
            String number = etCardNumber.getText().toString().trim();
            String expiry = etExpiry.getText().toString().trim();
            String cvv = etCVV.getText().toString().trim();
            String amount = etAmount.getText().toString().trim();

            if (number.isEmpty() || expiry.isEmpty() || cvv.isEmpty() || amount.isEmpty()) {
                Toast.makeText(this, "Điền đầy đủ thông tin thẻ và số tiền", Toast.LENGTH_SHORT).show();
                return;
            }

            tokenizeCard(number, expiry, cvv, amount);

//            String amount = etAmount.getText().toString().trim();
//            if (amount.isEmpty()) {
//                Toast.makeText(this, "Nhập số tiền", Toast.LENGTH_SHORT).show();
//                return;
//            }

//            // ✅ Tạo yêu cầu thanh toán
//            DropInRequest dropInRequest = new DropInRequest();
//
//            try {
//                dropInClient.launchDropIn(dropInRequest);
//            } catch (Exception e) {
//                Log.e("DROPIN", "❌ Không thể hiển thị Drop-In: " + e.getMessage());
//                Toast.makeText(this, "Không thể mở giao diện thanh toán", Toast.LENGTH_SHORT).show();
//            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        dropInClient = new DropInClient(PaymentActivity.this, clientToken);
        dropInClient.setListener(PaymentActivity.this);

    }

    private void fetchClientToken(ClientTokenCallback callback) {
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IBrainTreeApiService service = retrofit.create(IBrainTreeApiService.class);

        service.getClientToken().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        clientToken = response.body().string().trim();
                        Log.d("TOKEN", "✅ Token: " + clientToken);

                        braintreeClient = new BraintreeClient(PaymentActivity.this, clientToken);
                        cardClient = new CardClient(braintreeClient);

                        tvResult.setText("Token đã sẵn sàng.");
                        callback.onSuccess(clientToken);
                    } catch (IOException e) {
                        tvResult.setText("Lỗi đọc token");
                        callback.onFailure(e);
                    }
                } else {
                    tvResult.setText("Không lấy được token");
                    callback.onFailure(new Exception("Không lấy được token"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                tvResult.setText("Lỗi API: " + t.getMessage());
            }
        });

    }

    @Override
    public void onDropInSuccess(@NonNull DropInResult result) {
        String amount = etAmount.getText().toString().trim();
        if (result.getPaymentMethodNonce() != null) {
            String nonce = result.getPaymentMethodNonce().getString();
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

    private void tokenizeCard(String number, String expiry, String cvv, String amount) {
        Card card = new Card();
        card.setNumber(number);
        card.setExpirationDate(expiry); // ví dụ: "12/26"
        card.setCvv(cvv);

        cardClient.tokenize(card, (cardNonce, error) -> {
            if (error != null) {
                tvResult.setText("❌ Lỗi thẻ: " + error.getMessage());
                Log.e("CARD", "Tokenize lỗi", error);
                return;
            }

            Log.d("CARD", "✅ Nonce: " + cardNonce.getString());
            tvResult.setText("Thẻ OK: " + cardNonce.getString());
            sendNonceToBackend(amount, cardNonce.getString());
        });
    }

    private void sendNonceToBackend(String amount, String nonce) {
        Retrofit retrofit = ApiClient.getClientWithToken(this);
        IBrainTreeApiService service = retrofit.create(IBrainTreeApiService.class);

        Log.d("CHECKOUT", "Gửi nonce = " + nonce + ", amount = " + amount);

        service.checkout(nonce, amount).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PaymentActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                    tvResult.setText("✅ Thành công. Transaction ID: " + getTransactionId(response));
                } else {
                    Toast.makeText(PaymentActivity.this, "❌ BE lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                    tvResult.setText("BE lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "❌ Kết nối lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                tvResult.setText("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    private String getTransactionId(Response<ResponseBody> response) {
        try {
            return response.body() != null ? response.body().string() : "Không rõ";
        } catch (IOException e) {
            return "Không lấy được Transaction ID";
        }
    }
}
