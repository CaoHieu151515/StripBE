package com.example.strip.Activities.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strip.Activities.StripActivity;
import com.example.strip.Models.Request.LoginVM;
import com.example.strip.Models.Response.ResponseToken;
import com.example.strip.R;
import com.example.strip.Services.IAuthenticateApiService;
import com.example.strip.Utils.ErrorTranslate;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.network.ApiClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private EditText edEmail, edPassword;
    private NotificationPopup notificationPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Find the ImageView by its ID
        Button btnLogin = findViewById(R.id.btnSignIn);
        edEmail = findViewById(R.id.etUsername);
        edPassword = findViewById(R.id.etPassword);
        notificationPopup = new NotificationPopup(this);

        // Set an OnClickListener for the ImageView
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPassenger();
            }
        });
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, BeginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void loginPassenger() {
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        LoginVM request = new LoginVM(email, password, true);

        IAuthenticateApiService apiService = ApiClient.getClient().create(IAuthenticateApiService.class);
        Call<ResponseToken> call = apiService.login(request);
        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String jwtToken = response.body().getId_token(); // Ensure the response body is not null

                    // Store the JWT in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("jwtToken", jwtToken); // Use the same key as in CartFragment
                    editor.apply();

                    notificationPopup.showPopup("Đăng nhập thành công!", false);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(LoginActivity.this, StripActivity.class);
                        startActivity(intent);
                        finish();
                    }, 1500);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Đăng nhập thất bại: " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Đăng nhập thất bại: \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Đăng nhập thất bại: \n Không thể lấy thông báo lỗi", true);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }

}
