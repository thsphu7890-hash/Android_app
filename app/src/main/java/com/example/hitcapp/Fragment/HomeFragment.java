package com.example.hitcapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hitcapp.Adapter.CategoryAdapter;
import com.example.hitcapp.Adapter.ProductAdapter;
import com.example.hitcapp.DetailActivity;
import com.example.hitcapp.Model.Category;
import com.example.hitcapp.Model.Product;
import com.example.hitcapp.Network.RetrofitClient;
import com.example.hitcapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvCategories, rvHotProducts, rvAllProducts;
    private ProductAdapter hotAdapter, allAdapter;
    private CategoryAdapter categoryAdapter;

    // Base URL chuẩn trỏ vào folder uploads
    private static final String IMAGE_BASE_URL = "http://192.168.1.253:5000/uploads/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ ID
        rvCategories = view.findViewById(R.id.rv_categories);
        rvHotProducts = view.findViewById(R.id.rv_hot_products);
        rvAllProducts = view.findViewById(R.id.rv_all_products);

        // Cấu hình LayoutManager
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvHotProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvAllProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // KHỞI TẠO ADAPTER (Đã bỏ Glide.with(this) để khớp với ProductAdapter mới)
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            Toast.makeText(getContext(), "Hãng: " + category.getCategoryName(), Toast.LENGTH_SHORT).show();
        });

        // Chỉ truyền 2 tham số: List và Listener
        hotAdapter = new ProductAdapter(new ArrayList<>(), this::goToDetail);
        allAdapter = new ProductAdapter(new ArrayList<>(), this::goToDetail);

        rvCategories.setAdapter(categoryAdapter);
        rvHotProducts.setAdapter(hotAdapter);
        rvAllProducts.setAdapter(allAdapter);

        loadCategoriesFromApi();
        loadProductsFromApi();

        return view;
    }

    private void loadCategoriesFromApi() {
        RetrofitClient.getApiService().getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    categoryAdapter.setData(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Category Fail: " + t.getMessage());
            }
        });
    }

    private void loadProductsFromApi() {
        RetrofitClient.getApiService().getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    List<Product> productList = response.body();

                    // Hiển thị 5 sản phẩm đầu làm hàng HOT
                    List<Product> hotList = new ArrayList<>();
                    if (productList.size() > 5) {
                        hotList.addAll(productList.subList(0, 5));
                    } else {
                        hotList.addAll(productList);
                    }

                    hotAdapter.setData(hotList);
                    allAdapter.setData(productList);
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Log.e("API_ERROR", "Product Fail: " + t.getMessage());
                }
            }
        });
    }

    private void goToDetail(Product product) {
        Intent intent = new Intent(getContext(), DetailActivity.class);

        intent.putExtra("product_id", product.getId());
        intent.putExtra("product_name", product.getName());
        intent.putExtra("product_price", product.getPrice());
        intent.putExtra("product_description", product.getDescription());
        intent.putExtra("product_category", product.getCategoryName());

        // Nối link ảnh đầy đủ để trang DetailActivity load phát ăn ngay
        String fullImageUrl = IMAGE_BASE_URL + product.getImageUrl();
        intent.putExtra("product_image", fullImageUrl);

        startActivity(intent);
    }
}