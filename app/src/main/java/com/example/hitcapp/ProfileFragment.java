package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private RelativeLayout btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Ánh xạ ID của RelativeLayout (Nút đăng xuất)
        btnLogout = view.findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị thông báo
                Toast.makeText(getContext(), "Đang đăng xuất...", Toast.LENGTH_SHORT).show();

                // Chuyển sang màn hình Login
                // Lưu ý: Nhớ đổi LoginActivity thành tên class đăng nhập của Phú
                Intent intent = new Intent(getActivity(), LoginActivity.class);

                // Xóa sạch lịch sử các màn hình trước đó
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        return view;
    }
}