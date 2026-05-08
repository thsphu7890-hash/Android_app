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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView tvName, tvPrice, tvDesc, tvCategory;
    private ImageButton btnBack;
    private Button btnAddCart, btnBuyNow;
    private int productId;

    // ĐÃ SỬA: Khớp 100% với server và CategoryFragment
    private static final String IMAGE_BASE_URL = "http://192.168.1.253:5000/uploads/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        initViews();

        productId = getIntent().getIntExtra("product_id", -1);

        if (productId != -1) {
            loadProductDetailFromApi(productId);
        } else {
            getDataFromIntent();
        }

        setupEventListeners();
    }

    private void initViews() {
        imgProduct = findViewById(R.id.img_detail_product);
        tvName = findViewById(R.id.tv_detail_name);
        tvPrice = findViewById(R.id.tv_detail_price);
        tvDesc = findViewById(R.id.tv_detail_desc);
        tvCategory = findViewById(R.id.tv_detail_category);
        btnBack = findViewById(R.id.btn_back);
        btnAddCart = findViewById(R.id.btn_add_cart);
        btnBuyNow = findViewById(R.id.btn_buy_now);
    }

    private void loadProductDetailFromApi(int id) {
        RetrofitClient.getApiService().getProductDetail(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayProduct(response.body());
                } else {
                    getDataFromIntent();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                getDataFromIntent();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void displayProduct(Product product) {
        tvName.setText(product.getName());

        if (tvCategory != null) {
            tvCategory.setText("Hãng: " + (product.getCategoryName() != null ? product.getCategoryName() : "Khác"));
        }

        // Định dạng giá tiền chuẩn (3.500.000đ)
        try {
            double priceValue = Double.parseDouble(product.getPrice());
            tvPrice.setText(String.format("%,.0fđ", priceValue).replace(",", "."));
        } catch (Exception e) {
            tvPrice.setText(product.getPrice() + "đ");
        }

        tvDesc.setText(product.getDescription());

        // Nối link ảnh an toàn
        String imgName = product.getImageUrl();
        String fullImageUrl;
        if (imgName != null && imgName.startsWith("/")) {
            fullImageUrl = "http://192.168.1.253:5000/uploads" + imgName;
        } else {
            fullImageUrl = IMAGE_BASE_URL + imgName;
        }

        Glide.with(this)
                .load(fullImageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error_image)
                .into(imgProduct);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            tvName.setText(intent.getStringExtra("product_name"));
            tvPrice.setText(intent.getStringExtra("product_price"));
            tvDesc.setText(intent.getStringExtra("product_description"));

            if (tvCategory != null) {
                tvCategory.setText("Hãng: " + intent.getStringExtra("product_category"));
            }

            // Ở đây lấy trực tiếp vì bên Fragment đã gửi link FULL rồi
            Glide.with(this)
                    .load(intent.getStringExtra("product_image"))
                    .placeholder(R.drawable.loading)
                    .into(imgProduct);
        }
    }

    private void setupEventListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnAddCart.setOnClickListener(v -> {
            int userId = 1; // ID mẫu
            int quantity = 1;

            RetrofitClient.getApiService().addToCart(userId, productId, quantity).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(DetailActivity.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Không thể thêm vào giỏ!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(DetailActivity.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnBuyNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("product_id", productId);
            startActivity(intent);
        });
    }
}