package com.example.strip.Activities.Account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.strip.Models.Request.RegisterVM;
import com.example.strip.R;
import com.example.strip.Services.IAccountApiService;
import com.example.strip.Utils.ErrorTranslate;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.network.ApiClient;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {
    private EditText edPhone, edEmail, edPassword, edConfirmPassword;
    private NotificationPopup notificationPopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Find the ImageView by its ID
        Button btnRegister = findViewById(R.id.btnSignUp);
        edPhone = findViewById(R.id.etPhone);
        edEmail = findViewById(R.id.etEmail);
        edPassword = findViewById(R.id.etPassword);
        edConfirmPassword = findViewById(R.id.etConfirmPassword);
        notificationPopup = new NotificationPopup(this);
        // Set an OnClickListener for the ImageView
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CustomerHomeActivity
                registerPassenger();
            }
        });
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, BeginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void registerPassenger() {
        String phone = edPhone.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String confirmPassword = edConfirmPassword.getText().toString().trim();

        if (phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            notificationPopup.showPopup("Vui lòng điền đầy đủ thông tin",true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            notificationPopup.showPopup("Mật khẩu xác nhận không khớp",true);
            return;
        }
        RegisterVM request = new RegisterVM(email, password, "string",true,"string", phone);

        IAccountApiService apiService = ApiClient.getClient().create(IAccountApiService.class);
        Call<Void> call = apiService.register(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    notificationPopup.showPopup("Đăng ký thành công!", false);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                        intent.putExtra("login", "string");
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                        finish();
                    }, 1500);
                } else {
                    // Log the error response for debugging

                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Đăng ký thất bại: " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Đăng ký thất bại: \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Đăng ký thất bại: \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }
}
