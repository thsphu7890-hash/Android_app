package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hitcapp.Model.User;
import com.example.hitcapp.Model.UserRequest;
import com.example.hitcapp.Network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Ánh xạ View
        edtEmail = findViewById(R.id.edt_username); // ID trong XML của ông có thể là edt_username
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);

        // Nhận email từ màn hình đăng ký nếu có
        String registeredUser = getIntent().getStringExtra("REGISTERED_USER");
        if (registeredUser != null) {
            edtEmail.setText(registeredUser);
        }

        // 2. Sự kiện nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            handleLogin(email, pass);
        });

        // 3. Sự kiện chuyển sang màn hình Đăng ký
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin(String email, String password) {
        // Đóng gói dữ liệu vào UserRequest để khớp với ApiService
        // Giả sử Backend của ông dùng Email để đăng nhập, truyền email vào field username/email
        UserRequest loginRequest = new UserRequest("", email, password);

        Log.d("API_DEBUG", "Đang đăng nhập với: " + email);

        RetrofitClient.getApiService().login(loginRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                    Log.d("API_DEBUG", "Đăng nhập thành công: " + user.getUsername());
                    Toast.makeText(LoginActivity.this, "Chào mừng " + user.getUsername(), Toast.LENGTH_SHORT).show();

                    // Chuyển sang MainActivity và gửi kèm object User
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // LƯU Ý: Class User phải implements Serializable mới không bị báo đỏ ở đây
                    intent.putExtra("user_data", user);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("API_DEBUG", "Lỗi: " + response.code());
                    Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API_DEBUG", "Fail: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Lỗi kết nối máy chủ!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}