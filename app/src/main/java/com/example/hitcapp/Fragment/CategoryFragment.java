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
import androidx.recyclerview.widget.RecyclerView;

import com.example.hitcapp.Adapter.ProductAdapter;
import com.example.hitcapp.DetailActivity;
import com.example.hitcapp.Model.Product;
import com.example.hitcapp.Network.RetrofitClient;
import com.example.hitcapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private List<Product> productList;

    // Đã sửa: Khớp với app.use('/uploads', ...) trong Node.js của ông
    private static final String IMAGE_BASE_URL = "http://192.168.1.253:5000/uploads/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        rvProducts = view.findViewById(R.id.gv_cate);
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productList = new ArrayList<>();

        // Khởi tạo Adapter
        adapter = new ProductAdapter(productList, product -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);

            // Xử lý link ảnh an toàn (tránh lỗi thừa/thiếu dấu gạch chéo)
            String imgName = product.getImageUrl();
            String fullImageUrl;

            if (imgName != null && imgName.startsWith("/")) {
                fullImageUrl = "http://192.168.1.253:5000/uploads" + imgName;
            } else {
                fullImageUrl = IMAGE_BASE_URL + imgName;
            }

            // TRUYỀN DỮ LIỆU SANG DETAIL
            intent.putExtra("product_id", product.getId());
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_description", product.getDescription());
            intent.putExtra("product_category", product.getCategoryName());
            intent.putExtra("product_image", fullImageUrl); // Truyền link đã nối xong

            startActivity(intent);
        });

        rvProducts.setAdapter(adapter);
        loadProductsFromApi();

        return view;
    }

    private void loadProductsFromApi() {
        RetrofitClient.getApiService().getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (isAdded() && getActivity() != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        productList.clear();
                        productList.addAll(response.body());
                        adapter.setData(productList);
                        Log.d("API_DEBUG", "Load thành công " + productList.size() + " sản phẩm");
                    } else {
                        Log.e("API_DEBUG", "Lỗi phản hồi: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                if (isAdded() && getActivity() != null) {
                    Log.e("API_DEBUG", "Lỗi kết nối: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}