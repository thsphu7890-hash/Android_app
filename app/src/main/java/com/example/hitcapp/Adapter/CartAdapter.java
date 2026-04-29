package com.example.hitcapp.Adapter;

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

import java.util.List;

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

        // 1. Đổ dữ liệu văn bản
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("%sđ", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        // 2. Load ảnh từ API bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl()) // URL từ Model CartItem
                .placeholder(R.drawable.blue_shoes) // Ảnh hiện khi đang tải
                .error(R.drawable.blue_shoes)       // Ảnh hiện khi link lỗi
                .into(holder.imgProduct);

        // 3. Xử lý nút Tăng (+)
        holder.btnPlus.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            item.setQuantity(newQty);
            notifyItemChanged(holder.getAdapterPosition());
            mFragment.calculateTotal();
            // (Tùy chọn) Gọi API cập nhật số lượng trên server tại đây
        });

        // 4. Xử lý nút Giảm (-)
        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQty = item.getQuantity() - 1;
                item.setQuantity(newQty);
                notifyItemChanged(holder.getAdapterPosition());
                mFragment.calculateTotal();
                // (Tùy chọn) Gọi API cập nhật số lượng trên server tại đây
            }
        });

        // 5. Xử lý nút Xóa (Gọi hàm xóa của Fragment để xóa trên API)
        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // Gọi hàm xóa có kết nối API mà mình vừa viết ở CartFragment
                // item.getId() là ID của bản ghi giỏ hàng trong Database
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