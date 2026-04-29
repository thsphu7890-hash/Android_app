package com.example.hitcapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hitcapp.DetailActivity;
import com.example.hitcapp.R;

public class CategoryFragment extends Fragment {

    // Dữ liệu mẫu (sau này ông có thể thay bằng gọi API như bên HomeFragment)
    String[] names = {"Nike Air Max", "Adidas Forum", "Jordan 4 Retro", "Vans Old Skool", "Puma RS-X", "Converse 70s"};
    String[] prices = {"3.500.000đ", "2.800.000đ", "5.200.000đ", "1.500.000đ", "2.100.000đ", "1.900.000đ"};
    String[] descs = {
            "Dòng Air Max êm ái, phù hợp chạy bộ.",
            "Phong cách cổ điển, phối màu cực chất.",
            "Huyền thoại bóng rổ, sang trọng và đẳng cấp.",
            "Bền bỉ, cá tính cho các bạn trẻ.",
            "Thiết kế tương lai, hầm hố.",
            "Vẻ đẹp vượt thời gian, dễ phối đồ."
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // --- CHỖ CẦN SỬA ---
        // Ánh xạ đúng ID của GridView trong fragment_category.xml
        GridView gvProducts = view.findViewById(R.id.gv_cate);

        // Nếu GridView tìm thấy (không null) thì mới gán adapter
        if (gvProducts != null) {
            gvProducts.setAdapter(new CategoryProductAdapter());
        }

        return view;
    }

    private class CategoryProductAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // Đảm bảo file R.layout.item_product là file chứa ImageView và TextView
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
            }

            ImageView imgProduct = convertView.findViewById(R.id.img_product);
            TextView tvName = convertView.findViewById(R.id.tv_name);
            TextView tvPrice = convertView.findViewById(R.id.tv_price);

            tvName.setText(names[position]);
            tvPrice.setText(prices[position]);
            imgProduct.setImageResource(R.drawable.blue_shoes);

            // Click vào từng ô sản phẩm
            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("product_name", names[position]);
                intent.putExtra("product_price", prices[position]);
                intent.putExtra("product_desc", descs[position]);
                startActivity(intent);
            });

            return convertView;
        }
    }
}