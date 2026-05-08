package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.hitcapp.Model.User;
import com.example.hitcapp.Model.UserRequest;
import com.example.hitcapp.Network.RetrofitClient;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Ánh xạ View (Phải khớp với ID trong XML của ông)
        etUsername = findViewById(R.id.et_username); // Kiểm tra lại ID này
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);

        // 2. Thiết lập sự kiện Click
        btnRegister.setOnClickListener(v -> {
            Log.d("REGISTER_DEBUG", "Nút đăng ký đã được bấm");

            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            Log.d("REGISTER_DEBUG", "Data: " + username + " | " + email);

            // Kiểm tra rỗng
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                Log.d("REGISTER_DEBUG", "Lỗi: Để trống thông tin");
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra khớp mật khẩu
            if (!password.equals(confirmPass)) {
                Log.d("REGISTER_DEBUG", "Lỗi: Mật khẩu không khớp");
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nếu ổn thì gọi API
            handleRegister(username, email, password);
        });
    }

    private void handleRegister(String user, String mail, String pass) {
        // Tạo object request (đảm bảo UserRequest có Constructor tương ứng)
        UserRequest request = new UserRequest(user, mail, pass);

        Log.d("API_DEBUG", "Bắt đầu gọi API...");

        // Gọi API qua Retrofit
        RetrofitClient.getApiService().register(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("API_DEBUG", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_DEBUG", "Đăng ký thành công trên Server");
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    // Trả kết quả về LoginActivity để tự điền username
                    Intent intent = new Intent();
                    intent.putExtra("REGISTERED_USER", user);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Log.e("API_DEBUG", "Lỗi server hoặc trùng user/email");
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại! (Code: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API_DEBUG", "Lỗi kết nối nghiêm trọng: " + t.getMessage());
                Toast.makeText(RegisterActivity.this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}