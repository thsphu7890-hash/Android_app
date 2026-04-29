package com.example.hitcapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hitcapp.Adapter.CartAdapter;
import com.example.hitcapp.CheckoutActivity;
import com.example.hitcapp.Model.CartItem;
import com.example.hitcapp.Network.RetrofitClient;
import com.example.hitcapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private LinearLayout layoutEmptyCart;
    private List<CartItem> cartList;
    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // 1. Ánh xạ các View
        rvCartItems = view.findViewById(R.id.rv_cart_items);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        layoutEmptyCart = view.findViewById(R.id.layout_empty_cart);
        Button btnCheckout = view.findViewById(R.id.btn_checkout);

        // 2. Setup RecyclerView ban đầu
        cartList = new ArrayList<>();
        adapter = new CartAdapter(cartList, this);
        rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCartItems.setAdapter(adapter);

        // 3. Sự kiện Thanh toán
        btnCheckout.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(getActivity(), "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                intent.putExtra("total_price", tvTotalPrice.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load lại data mỗi khi quay lại Fragment
        fetchCartData();
    }

    private void fetchCartData() {
        // Tạm thời fix cứng userId = 1 để test
        RetrofitClient.getApiService().getCart(1).enqueue(new Callback<List<CartItem>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<CartItem>> call, @NonNull Response<List<CartItem>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    cartList.clear();
                    cartList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    calculateTotal();
                    checkCartStatus();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CartItem>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Log.e("API_ERROR", "Cart fail: " + t.getMessage());
                    checkCartStatus();
                }
            }
        });
    }

    // HÀM XÓA ITEM - Sửa lại để mượt và chính xác
    public void removeItemFromCart(int cartId, int position) {
        RetrofitClient.getApiService().removeFromCart(cartId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // Kiểm tra bounds để tránh crash App
                    if (position >= 0 && position < cartList.size()) {
                        cartList.remove(position);

                        // Thông báo xóa item kèm hiệu ứng mượt
                        adapter.notifyItemRemoved(position);

                        // Cập nhật lại dải index cho các item còn lại
                        adapter.notifyItemRangeChanged(position, cartList.size());

                        calculateTotal();
                        checkCartStatus();
                        Toast.makeText(getContext(), "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể xóa trên Server!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối khi xóa!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    public void calculateTotal() {
        long total = 0;
        for (CartItem item : cartList) {
            try {
                // Regex lấy số từ chuỗi price
                String priceStr = item.getPrice().replaceAll("[^0-9]", "");
                long price = Long.parseLong(priceStr);
                total += price * item.getQuantity();
            } catch (Exception e) {
                Log.e("PARSE_ERROR", "Lỗi tính tiền: " + e.getMessage());
            }
        }
        tvTotalPrice.setText(String.format("%,dđ", total).replace(",", "."));
    }

    public void checkCartStatus() {
        if (cartList.isEmpty()) {
            layoutEmptyCart.setVisibility(View.VISIBLE);
            rvCartItems.setVisibility(View.GONE);
        } else {
            layoutEmptyCart.setVisibility(View.GONE);
            rvCartItems.setVisibility(View.VISIBLE);
        }
    }
}