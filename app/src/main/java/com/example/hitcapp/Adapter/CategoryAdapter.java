package com.example.hitcapp.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hitcapp.Model.Category;
import com.example.hitcapp.R;

import java.util.List;

/**
 * Adapter hiển thị danh sách Hãng giày (Categories) dưới dạng các nút Chip bo tròn.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private final OnCategoryClickListener listener;

    // Interface để xử lý sự kiện khi người dùng chọn hãng (ví dụ lọc giày Nike)
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(List<Category> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    /**
     * Cập nhật lại danh sách dữ liệu khi gọi API xong
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Category> newList) {
        this.categoryList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp file giao diện item_category.xml ông đã tạo
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if (category == null) return;

        // Hiển thị tên hãng từ Database (Nike, Adidas, Jordan...)
        holder.tvCategoryName.setText(category.getCategoryName());

        // Xử lý sự kiện Click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category);
                // Log để ông kiểm tra trong Logcat xem click đúng hãng chưa
                Log.d("CLICK_CATEGORY", "User selected: " + category.getCategoryName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (categoryList != null) ? categoryList.size() : 0;
    }

    /**
     * Lớp giữ các View trong layout giúp tăng hiệu năng
     */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ file item_category.xml
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);

            // Thêm hiệu ứng gợn sóng khi chạm vào nút (nếu CardView chưa có)
            itemView.setClickable(true);
            itemView.setFocusable(true);
        }
    }
}