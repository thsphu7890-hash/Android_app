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

import com.bumptech.glide.Glide;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ ID từ XML
        rvCategories = view.findViewById(R.id.rv_categories);
        rvHotProducts = view.findViewById(R.id.rv_hot_products);
        rvAllProducts = view.findViewById(R.id.rv_all_products);

        // Cấu hình hiển thị (LayoutManager)
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvHotProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvAllProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Khởi tạo Adapter
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            Toast.makeText(getContext(), "Hãng: " + category.getCategoryName(), Toast.LENGTH_SHORT).show();
        });

        hotAdapter = new ProductAdapter(new ArrayList<>(), Glide.with(this), this::goToDetail);
        allAdapter = new ProductAdapter(new ArrayList<>(), Glide.with(this), this::goToDetail);

        // Gán Adapter
        rvCategories.setAdapter(categoryAdapter);
        rvHotProducts.setAdapter(hotAdapter);
        rvAllProducts.setAdapter(allAdapter);

        // Tải dữ liệu từ API
        loadCategoriesFromApi();
        loadProductsFromApi();

        return view;
    }

    private void loadCategoriesFromApi() {
        RetrofitClient.getApiService().getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    categoryAdapter.setData(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API_ERROR", "Category Fail: " + t.getMessage());
            }
        });
    }

    private void loadProductsFromApi() {
        RetrofitClient.getApiService().getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    List<Product> productList = response.body();

                    // Logic lấy sản phẩm HOT
                    List<Product> hotList = new ArrayList<>();
                    if (productList.size() > 5) hotList.addAll(productList.subList(0, 5));
                    else hotList.addAll(productList);

                    hotAdapter.setData(hotList);
                    allAdapter.setData(productList);
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (isAdded()) Toast.makeText(getContext(), "Lỗi server sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToDetail(Product product) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("product_id", product.getId());
        intent.putExtra("product_name", product.getName());
        intent.putExtra("product_image", product.getImageUrl());
        startActivity(intent);
    }
}