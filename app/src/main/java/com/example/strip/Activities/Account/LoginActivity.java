package com.example.strip.Activities.Account;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private EditText edEmail, edPassword;
    private NotificationPopup notificationPopup;
    private CheckBox cbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnSignIn);
        edEmail = findViewById(R.id.etUsername);
        edPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        notificationPopup = new NotificationPopup(this);

        // Ngăn bàn phím xuất hiện khi click và hiển thị popup tài khoản
        edEmail.setOnClickListener(view -> showSavedAccountsPopup(view));

        // Nút đăng nhập
        btnLogin.setOnClickListener(v -> loginPassenger());

        // Nút quay lại
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, BeginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginPassenger() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            notificationPopup.showPopup("Vui lòng nhập đầy đủ email và mật khẩu!", true);
            return;
        }

        LoginVM request = new LoginVM(email, password, true);

        IAuthenticateApiService apiService = ApiClient.getClient().create(IAuthenticateApiService.class);
        Call<ResponseToken> call = apiService.login(request);
        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String jwtToken = response.body().getId_token();

                    // Lưu token vào SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    sharedPreferences.edit().putString("jwtToken", jwtToken).apply();

                    // Lưu email & password nếu checkbox được chọn
                    if (cbRemember.isChecked()) {
                        saveLoginInfo(email, password);
                    }

                    notificationPopup.showPopup("Đăng nhập thành công!", false);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(LoginActivity.this, StripActivity.class);
                        startActivity(intent);
                        finish();
                    }, 1500);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
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

    private void saveLoginInfo(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("SavedAccounts", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(email, password); // Ghi đè nếu email đã có
        editor.apply();
    }

    private void showSavedAccountsPopup(View anchorView) {
        SharedPreferences sharedPreferences = getSharedPreferences("SavedAccounts", MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        if (allEntries.isEmpty()) {
            Toast.makeText(this, "Không có tài khoản đã lưu", Toast.LENGTH_SHORT).show();
            return;
        }

        PopupMenu popupMenu = new PopupMenu(this, anchorView);
        int id = 0;
        for (String email : allEntries.keySet()) {
            popupMenu.getMenu().add(0, id++, 0, email);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            String selectedEmail = item.getTitle().toString();
            String password = sharedPreferences.getString(selectedEmail, "");

            // Hiển thị menu phụ: Đăng nhập / Xóa
            PopupMenu subMenu = new PopupMenu(LoginActivity.this, anchorView);
            subMenu.getMenu().add("Sử dụng tài khoản này");
            subMenu.getMenu().add("Xoá tài khoản");

            subMenu.setOnMenuItemClickListener(subItem -> {
                if (subItem.getTitle().equals("Sử dụng tài khoản này")) {
                    edEmail.setText(selectedEmail);
                    edPassword.setText(password);
                } else if (subItem.getTitle().equals("Xoá tài khoản")) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Xác nhận")
                            .setMessage("Bạn có chắc chắn muốn xoá tài khoản này?")
                            .setPositiveButton("Xoá", (dialog, which) -> {
                                sharedPreferences.edit().remove(selectedEmail).apply();
                                Toast.makeText(LoginActivity.this, "Đã xoá tài khoản", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Huỷ", null)
                            .show();
                }
                return true;
            });

            subMenu.show();
            return true;
        });

        popupMenu.show();
    }

}
