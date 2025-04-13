package com.example.strip.Activities.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strip.Activities.StripActivity;
import com.example.strip.Models.Request.LoginVM;
import com.example.strip.Models.Response.ResponseToken;
import com.example.strip.R;
import com.example.strip.Services.IAuthenticateApiService;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmail, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Find the ImageView by its ID
        Button btnLogin = findViewById(R.id.btnSignIn);
        edEmail = findViewById(R.id.etUsername);
        edPassword = findViewById(R.id.etPassword);

        // Set an OnClickListener for the ImageView
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPassenger();
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

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, StripActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: Không thể lấy thông báo lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
