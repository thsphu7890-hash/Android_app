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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private LinearLayout layoutEmptyCart;
    private List<CartItem> cartList;
    private CartAdapter adapter;

    public static final String IMAGE_BASE_URL = "http://192.168.1.253:5000/uploads/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCartItems = view.findViewById(R.id.rv_cart_items);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        layoutEmptyCart = view.findViewById(R.id.layout_empty_cart);
        Button btnCheckout = view.findViewById(R.id.btn_checkout);

        cartList = new ArrayList<>();
        adapter = new CartAdapter(cartList, this);
        rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCartItems.setAdapter(adapter);

        btnCheckout.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(getActivity(), "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                // Gửi số tiền đã format sang Checkout
                intent.putExtra("total_price", tvTotalPrice.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCartData();
    }

    private void fetchCartData() {
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
                    Log.e("API_ERROR", "Lỗi tải giỏ hàng: " + t.getMessage());
                    checkCartStatus();
                }
            }
        });
    }

    public void removeItemFromCart(int cartId, int position) {
        RetrofitClient.getApiService().removeFromCart(cartId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    if (position >= 0 && position < cartList.size()) {
                        cartList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, cartList.size());
                        calculateTotal();
                        checkCartStatus();
                        Toast.makeText(getContext(), "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * HÀM TÍNH TIỀN ĐÃ ĐƯỢC CẬP NHẬT
     * Sử dụng DecimalFormat để đảm bảo phân cách hàng nghìn bằng dấu chấm chuẩn VN.
     */
    public void calculateTotal() {
        double total = 0;
        for (CartItem item : cartList) {
            try {
                // Giá từ API thường là chuỗi số "3500000.00"
                double price = Double.parseDouble(item.getPrice());
                total += price * item.getQuantity();
            } catch (Exception e) {
                Log.e("PARSE_ERROR", "Lỗi định dạng giá: " + item.getPrice());
            }
        }

        // Cấu hình định dạng: dấu chấm phân cách hàng nghìn
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        String formattedPrice = decimalFormat.format(total);

        // Hiển thị: 3.500.000đ
        tvTotalPrice.setText(formattedPrice + "đ");
    }

    public void checkCartStatus() {
        if (cartList.isEmpty()) {
            layoutEmptyCart.setVisibility(View.VISIBLE);
            rvCartItems.setVisibility(View.GONE);
            tvTotalPrice.setText("0đ");
        } else {
            layoutEmptyCart.setVisibility(View.GONE);
            rvCartItems.setVisibility(View.VISIBLE);
        }
    }
}