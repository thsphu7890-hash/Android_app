package com.example.hitcapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> mList;
    private CartFragment mFragment;

    // Constructor: Truyền danh sách giày và Fragment vào đây
    public CartAdapter(List<CartItem> mList, CartFragment fragment) {
        this.mList = mList;
        this.mFragment = fragment;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp giao diện item_cart.xml vào
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = mList.get(position);
        if (item == null) return;

        // Đổ dữ liệu offline (Tạm thời dùng ảnh mặc định)
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("%sđ", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        // holder.imgProduct.setImageResource(item.getImageRes()); // Nếu Phú có ảnh trong drawable

        // 1. Xử lý nút Tăng (+)
        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position); // Vẽ lại dòng này
            mFragment.calculateTotal();   // Báo Fragment tính lại tổng tiền ở dưới
        });

        // 2. Xử lý nút Giảm (-)
        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                mFragment.calculateTotal();
            }
        });

        // 3. Xử lý nút Xóa
        holder.btnDelete.setOnClickListener(v -> {
            mList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mList.size());
            mFragment.calculateTotal();
            mFragment.checkCartStatus(); // Nếu xóa hết thì hiện trang trống (cái hình kính lúp)
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    // Lớp giữ các ID linh kiện trong item_cart.xml
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