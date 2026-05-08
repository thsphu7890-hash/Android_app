package com.example.hitcapp.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.hitcapp.Model.Product;
import com.example.hitcapp.R;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private final OnProductClickListener listener;

    // URL này phải khớp với cấu hình server Node.js của ông
    private static final String IMAGE_BASE_URL = "http://192.168.1.253:5000/uploads/";

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    // Tui đã bỏ RequestManager glide để ông khởi tạo Fragment cho nhẹ nợ
    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) return;

        // Đổ data vào ID từ XML ông gửi
        holder.tvName.setText(product.getName());
        holder.tvCategory.setText(product.getCategoryName());

        // Format giá tiền
        try {
            double priceValue = Double.parseDouble(product.getPrice());
            holder.tvPrice.setText(String.format("%,.0fđ", priceValue).replace(",", "."));
        } catch (Exception e) {
            holder.tvPrice.setText(product.getPrice() + "đ");
        }

        // Xử lý hình ảnh - Nối link chuẩn
        String fullImageUrl = IMAGE_BASE_URL + product.getImageUrl();

        Glide.with(holder.itemView.getContext())
                .load(fullImageUrl)
                .placeholder(R.drawable.blue_shoes) // Ảnh mặc định trong item_product.xml
                .error(R.drawable.blue_shoes)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgProduct);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvCategory;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ đúng ID từ cái CardView ông gửi
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            tvCategory = itemView.findViewById(R.id.tv_product_category);
            imgProduct = itemView.findViewById(R.id.img_product);
        }
    }
}