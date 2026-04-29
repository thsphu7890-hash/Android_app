package com.example.hitcapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hitcapp.Model.Product;
import com.example.hitcapp.Network.RetrofitClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView tvName, tvPrice, tvDesc;
    private ImageButton btnBack;
    private Button btnAddCart, btnBuyNow;
    private int productId; // Lưu ID để dùng cho các thao tác khác như Add Cart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        initViews();

        // Nhận ID sản phẩm từ Intent
        productId = getIntent().getIntExtra("product_id", -1);

        if (productId != -1) {
            loadProductDetailFromApi(productId);
        } else {
            // Nếu không có ID, dùng dữ liệu cũ truyền qua intent làm fallback
            getDataFromIntent();
        }

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

    // GỌI API ĐỂ LẤY DỮ LIỆU CHI TIẾT MỚI NHẤT
    private void loadProductDetailFromApi(int id) {
        RetrofitClient.getApiService().getProductDetail(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    displayProduct(product);
                } else {
                    Toast.makeText(DetailActivity.this, "Không thể tải chi tiết sản phẩm!", Toast.LENGTH_SHORT).show();
                    getDataFromIntent(); // Lỗi API thì dùng tạm data từ Intent
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Log.e("API_ERROR", Objects.requireNonNull(t.getMessage()));
                getDataFromIntent();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void displayProduct(Product product) {
        tvName.setText(product.getName());
        tvPrice.setText(String.format("%,.0fđ", product.getPrice())); // Định dạng tiền tệ
        tvDesc.setText(product.getDescription());

        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(R.drawable.blue_shoes)
                .into(imgProduct);
    }

    // Giữ lại hàm này để phòng hờ trường hợp API lỗi
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            tvName.setText(intent.getStringExtra("product_name"));
            tvPrice.setText(intent.getStringExtra("product_price"));
            tvDesc.setText(intent.getStringExtra("product_desc"));
            Glide.with(this).load(intent.getStringExtra("product_image")).into(imgProduct);
        }
    }

    private void setupEventListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnAddCart.setOnClickListener(v -> {
            // Sau này Phú sẽ gọi API @POST("api/cart") ở đây với productId
            Toast.makeText(this, "Đã thêm " + tvName.getText() + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });

        btnBuyNow.setOnClickListener(v -> {
            Toast.makeText(this, "Đang xử lý đơn hàng cho ID: " + productId, Toast.LENGTH_SHORT).show();
        });
    }
}