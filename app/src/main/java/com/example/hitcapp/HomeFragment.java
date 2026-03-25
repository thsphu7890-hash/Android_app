package com.example.hitcapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvHotProducts, rvAllProducts;
    private ProductAdapter productAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Ánh xạ ID
        rvHotProducts = view.findViewById(R.id.rv_hot_products);
        rvAllProducts = view.findViewById(R.id.rv_all_products);

        // 2. Tạo dữ liệu mẫu (Sau này Phú lấy từ Database/API)
        List<Product> list = new ArrayList<>();
        list.add(new Product("Nike Air Max", "2.500.000đ", R.drawable.blue_shoes));
        list.add(new Product("Adidas Ultra", "3.200.000đ", R.drawable.blue_shoes));
        list.add(new Product("Puma RS-X", "1.800.000đ", R.drawable.blue_shoes));
        list.add(new Product("Jordan 1 Retro", "5.500.000đ", R.drawable.blue_shoes));

        productAdapter = new ProductAdapter(list);

        // 3. Cấu hình Giày Hot (Chạy ngang)
        rvHotProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvHotProducts.setAdapter(productAdapter);

        // 4. Cấu hình Tất cả sản phẩm (Lưới 2 cột)
        rvAllProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvAllProducts.setAdapter(productAdapter);

        return view;
    }
}