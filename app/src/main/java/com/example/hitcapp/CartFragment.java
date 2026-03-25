package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private LinearLayout layoutEmptyCart;
    private Button btnCheckout; // Phải khai báo biến ở đây
    private CartAdapter adapter;
    private List<CartItem> cartList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // 1. Ánh xạ các View từ XML
        rvCartItems = view.findViewById(R.id.rv_cart_items);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        layoutEmptyCart = view.findViewById(R.id.layout_empty_cart);
        btnCheckout = view.findViewById(R.id.btn_checkout); // Ánh xạ nút thanh toán

        // 2. Cài đặt sự kiện Click cho nút thanh toán
        btnCheckout.setOnClickListener(v -> {
            if (cartList == null || cartList.isEmpty()) {
                Toast.makeText(getActivity(), "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
            } else {
                // Chuyển màn hình sang CheckoutActivity
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                intent.putExtra("total_price", tvTotalPrice.getText().toString());
                startActivity(intent);
            }
        });

        // 3. Khởi tạo dữ liệu mẫu
        initData();

        return view;
    }

    private void initData() {
        cartList = new ArrayList<>();
        // Phú nhớ check kỹ tên R.drawable.blue_shoes có tồn tại không nhé
        cartList.add(new CartItem("Nike Air Max 270", "3.500.000", 1, R.drawable.blue_shoes));
        cartList.add(new CartItem("Adidas Forum Low", "2.800.000", 2, R.drawable.blue_shoes));

        adapter = new CartAdapter(cartList, this);
        rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCartItems.setAdapter(adapter);

        calculateTotal();
        checkCartStatus();
    }

    public void calculateTotal() {
        if (cartList == null) return;
        long total = 0;
        for (CartItem item : cartList) {
            try {
                String priceStr = item.getPrice().replace(".", "").replace("đ", "").trim();
                long price = Long.parseLong(priceStr);
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        tvTotalPrice.setText(String.format("%,dđ", total).replace(",", "."));
    }

    public void checkCartStatus() {
        if (cartList == null || cartList.isEmpty()) {
            layoutEmptyCart.setVisibility(View.VISIBLE);
            rvCartItems.setVisibility(View.GONE);
        } else {
            layoutEmptyCart.setVisibility(View.GONE);
            rvCartItems.setVisibility(View.VISIBLE);
        }
    }
}