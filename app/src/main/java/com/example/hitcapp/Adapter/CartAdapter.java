package com.example.hitcapp.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hitcapp.Fragment.CartFragment;
import com.example.hitcapp.Model.CartItem;
import com.example.hitcapp.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<CartItem> mList;
    private final CartFragment mFragment;

    public CartAdapter(List<CartItem> mList, CartFragment fragment) {
        this.mList = mList;
        this.mFragment = fragment;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = mList.get(position);
        if (item == null) return;

        // 1. Đổ dữ liệu văn bản và định dạng giá tiền (Ví dụ: 3.500.000đ)
        holder.tvName.setText(item.getName());

        try {
            double priceValue = Double.parseDouble(item.getPrice());
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
            symbols.setGroupingSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
            holder.tvPrice.setText(decimalFormat.format(priceValue) + "đ");
        } catch (Exception e) {
            holder.tvPrice.setText(item.getPrice() + "đ");
        }

        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        // 2. Load ảnh từ API (Đã sửa link /uploads/)
        String imageUrl = item.getImageUrl();
        String fullImageUrl;
        if (imageUrl != null && imageUrl.startsWith("/")) {
            fullImageUrl = "http://192.168.1.253:5000/uploads" + imageUrl;
        } else {
            fullImageUrl = CartFragment.IMAGE_BASE_URL + imageUrl;
        }

        Glide.with(holder.itemView.getContext())
                .load(fullImageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error_image)
                .into(holder.imgProduct);

        // 3. Xử lý nút Tăng (+)
        holder.btnPlus.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                int newQty = item.getQuantity() + 1;
                item.setQuantity(newQty);
                notifyItemChanged(currentPos);
                mFragment.calculateTotal(); // Cập nhật tổng tiền ở dưới cùng
                // Ông nên gọi thêm API UpdateQuantity ở đây để lưu vào DB nhé
            }
        });

        // 4. Xử lý nút Giảm (-)
        holder.btnMinus.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION && item.getQuantity() > 1) {
                int newQty = item.getQuantity() - 1;
                item.setQuantity(newQty);
                notifyItemChanged(currentPos);
                mFragment.calculateTotal();
            }
        });

        // 5. Xử lý nút Xóa
        // Trong onBindViewHolder của CartAdapter.java
        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // Sửa getCartId() thành getId() vì Model của ông đặt tên là id
                mFragment.removeItemFromCart(item.getId(), currentPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQuantity;
        ImageButton btnPlus, btnMinus, btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_cart_product);
            tvName = itemView.findViewById(R.id.tv_cart_name);
            tvPrice = itemView.findViewById(R.id.tv_cart_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnDelete = itemView.findViewById(R.id.btn_cart_delete);
        }
    }
}