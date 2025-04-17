package com.example.strip.Activities.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strip.Activities.Intro.IntroFiveActivity;
import com.example.strip.Activities.Intro.IntroFourActivity;
import com.example.strip.Activities.StripActivity;
import com.example.strip.Models.Request.RegisterVM;
import com.example.strip.Models.Response.ResponseMessage;
import com.example.strip.R;
import com.example.strip.Services.IAccountApiService;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private EditText edLogin, edEmail, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Find the ImageView by its ID
        Button btnRegister = findViewById(R.id.btnSignUp);
        edLogin = findViewById(R.id.etLogin);
        edEmail = findViewById(R.id.etEmail);
        edPassword = findViewById(R.id.etPassword);
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
        String login = edLogin.getText().toString();
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();

        RegisterVM request = new RegisterVM(email, password, login,true,"string");

        IAccountApiService apiService = ApiClient.getClient().create(IAccountApiService.class);

        Call<Void> call = apiService.register(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                    intent.putExtra("login", login);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    // Log the error response for debugging
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("RegisterError", "Đăng ký thất bại: " + errorBody);
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: Không thể lấy thông báo lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("RegisterFailure", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(RegisterActivity.this, "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
