package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        // Xử lý ID 'img_welcome' thay vì 'main' để tránh crash nếu XML chưa đặt ID tổng
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.img_welcome), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tìm nút "Bắt đầu ngay" đã tạo trong XML
        Button btnGetStarted = findViewById(R.id.btn_get_started);

        // Thiết lập sự kiện khi nhấn nút
        btnGetStarted.setOnClickListener(v -> {
            // Chuyển từ WelcomeActivity sang LoginActivity
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);

            // finish() để người dùng không thể bấm nút quay lại (Back) về màn hình chào nữa
            finish();
        });
    }
}