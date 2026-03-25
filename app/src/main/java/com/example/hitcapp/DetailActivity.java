package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    // Khai báo các View để ánh xạ
    private ImageView imgProduct;
    private TextView tvName, tvPrice, tvDesc;
    private ImageButton btnBack;
    private Button btnAddCart, btnBuyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        // 1. Ánh xạ các ID từ XML (Phải khớp chính xác ID trong activity_detail.xml)
        initViews();

        // 2. Nhận dữ liệu từ Intent gửi sang
        getDataFromIntent();

        // 3. Xử lý các sự kiện Click
        setupEventListeners();
    }

    private void initViews() {
        imgProduct = findViewById(R.id.img_detail_product);
        tvName = findViewById(R.id.tv_detail_name);
        tvPrice = findViewById(R.id.tv_detail_price);
        tvDesc = findViewById(R.id.tv_detail_desc);
        btnBack = findViewById(R.id.btn_back);
        btnAddCart = findViewById(R.id.btn_add_cart);
        btnBuyNow = findViewById(R.id.btn_buy_now);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("ten_giay");
            String price = intent.getStringExtra("gia_giay");
            String desc = intent.getStringExtra("mo_ta");
            // Nếu có truyền ảnh thì nhận ở đây (tạm thời để mặc định blue_shoes)
            int imageRes = intent.getIntExtra("hinh_anh", R.drawable.blue_shoes);

            // Hiển thị lên giao diện
            tvName.setText(name != null ? name : "Tên sản phẩm");
            tvPrice.setText(price != null ? price : "0đ");
            tvDesc.setText(desc != null ? desc : "Chưa có mô tả cho sản phẩm này.");
            imgProduct.setImageResource(imageRes);
        }
    }

    private void setupEventListeners() {
        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Nút thêm vào giỏ hàng
        btnAddCart.setOnClickListener(v -> {
            String productName = tvName.getText().toString();
            Toast.makeText(this, "Đã thêm " + productName + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });

        // Nút mua ngay
        btnBuyNow.setOnClickListener(v -> {
            Toast.makeText(this, "Đang chuyển đến trang thanh toán...", Toast.LENGTH_SHORT).show();
        });
    }
}