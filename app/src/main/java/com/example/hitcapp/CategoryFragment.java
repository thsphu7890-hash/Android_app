package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        GridView gvCate = view.findViewById(R.id.gv_cate);

        // Phú gỡ bỏ cái setOnItemClickListener ở đây đi, mình sẽ bắt click trong Adapter cho chắc
        gvCate.setAdapter(new ProductAdapter());

        return view;
    }

    private class ProductAdapter extends BaseAdapter {
        @Override
        public int getCount() { return names.length; }

        @Override
        public Object getItem(int i) { return names[i]; }

        @Override
        public long getItemId(int i) { return i; }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // Sử dụng layout item mà Phú đã gửi
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
            }

            // 1. Ánh xạ các View
            ImageView img = convertView.findViewById(R.id.img_product);
            TextView tvName = convertView.findViewById(R.id.tv_name);
            TextView tvPrice = convertView.findViewById(R.id.tv_price);

            // 2. Đổ dữ liệu tạm thời
            tvName.setText(names[i]);
            tvPrice.setText(prices[i]);
            img.setImageResource(R.drawable.blue_shoes);

            // 3. SỬA TẠI ĐÂY: Bắt sự kiện click trực tiếp lên toàn bộ Item
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chuyển sang DetailActivity
                    Intent intent = new Intent(getActivity(), DetailActivity.class);

                    // Truyền dữ liệu theo vị trí i
                    intent.putExtra("ten_giay", names[i]);
                    intent.putExtra("gia_giay", prices[i]);
                    intent.putExtra("mo_ta", descs[i]);

                    // Vì bạn đang ở Fragment nên dùng startActivity trực tiếp hoặc thông qua getActivity()
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}