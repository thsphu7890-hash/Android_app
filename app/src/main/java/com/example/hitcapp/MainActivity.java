package com.example.hitcapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // ============================================================
        // DÒNG QUAN TRỌNG NHẤT: Gỡ bỏ lớp màu phủ để hiện icon gốc
        // Nếu không có dòng này, icon của Phú sẽ bị biến thành ô vuông đen/xanh
        bottomNav.setItemIconTintList(null);
        // ============================================================

        // 1. Mặc định mở app là hiện HomeFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        // 2. Xử lý khi bấm chuyển Tab (Navigation)
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            }
            else if (id == R.id.nav_category) {
                selectedFragment = new CategoryFragment();
            }
            else if (id == R.id.nav_cart) {
                selectedFragment = new CartFragment();
            }
            else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });
    }
}