package com.example.hitcapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hitcapp.LoginActivity;
import com.example.hitcapp.Model.User;
import com.example.hitcapp.R;

public class ProfileFragment extends Fragment {

    private RelativeLayout btnLogout, menuHistory, menuAddress;
    private TextView tvUserName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 1. Ánh xạ View từ file XML của ông
        tvUserName = view.findViewById(R.id.tv_user_name);
        btnLogout = view.findViewById(R.id.btn_logout);
        menuHistory = view.findViewById(R.id.menu_history);
        menuAddress = view.findViewById(R.id.menu_address);

        // 2. Lấy dữ liệu User truyền từ Login -> MainActivity -> Fragment
        if (getActivity() != null && getActivity().getIntent() != null) {
            User user = (User) getActivity().getIntent().getSerializableExtra("user_data");
            if (user != null) {
                // Hiển thị tên từ cột 'username' trong Database
                tvUserName.setText(user.getUsername());
            }
        }

        // 3. Xử lý sự kiện click các Menu (Lịch sử, Địa chỉ)
        menuHistory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Tính năng Lịch sử đang phát triển", Toast.LENGTH_SHORT).show();
        });

        // 4. Xử lý Đăng xuất
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            // Xóa sạch bộ nhớ các màn hình trước để không back lại được
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        return view;
    }
}