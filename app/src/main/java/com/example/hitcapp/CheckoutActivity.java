package com.example.hitcapp;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class CheckoutActivity extends AppCompatActivity {

    private EditText edtName, edtPhone, edtAddress;
    private TextView tvTotal;
    private RadioGroup rgPayment;
    private Button btnConfirm, btnCancel;
    private String paymentMethod = "COD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();

        // 1. Nhận dữ liệu tổng tiền (Đã được format bên CartFragment)
        String totalPrice = getIntent().getStringExtra("total_price");
        if (totalPrice != null) {
            tvTotal.setText(totalPrice);
        }

        // 2. Xử lý chọn thanh toán
        rgPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_cod) {
                paymentMethod = "COD";
            } else if (checkedId == R.id.rb_bank) {
                paymentMethod = "Chuyển khoản";
                showQRCodeDialog();
            }
        });

        btnCancel.setOnClickListener(v -> {
            Toast.makeText(this, "Đã hủy giao dịch", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnConfirm.setOnClickListener(v -> confirmOrder());
    }

    private void initViews() {
        edtName = findViewById(R.id.edt_name);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        tvTotal = findViewById(R.id.tv_checkout_total);
        rgPayment = findViewById(R.id.rg_payment_methods);
        btnConfirm = findViewById(R.id.btn_confirm_order);
        btnCancel = findViewById(R.id.btn_cancel_order);
    }

    private void showQRCodeDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_qr_payment);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        ImageView imgQR = dialog.findViewById(R.id.img_qr_code);
        TextView tvContent = dialog.findViewById(R.id.tv_qr_content);
        Button btnDone = dialog.findViewById(R.id.btn_close_qr);

        // Xử lý tiền để gắn vào link VietQR (Xóa dấu chấm và chữ đ)
        String rawAmount = tvTotal.getText().toString()
                .replace(".", "")
                .replace("đ", "")
                .trim();

        // Thay STK MB của Phú vào đây để nhận tiền nhé
        String stkMB = "123456789";
        String addInfo = "HitCApp_ThanhToan"; // Không dấu cho chắc

        String qrUrl = "https://img.vietqr.io/image/mbbank-" + stkMB + "-compact.png?amount="
                + rawAmount + "&addInfo=" + addInfo;

        Glide.with(this)
                .load(qrUrl)
                .placeholder(R.drawable.loading)
                .into(imgQR);

        tvContent.setText("Số tiền: " + tvTotal.getText().toString());

        btnDone.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void confirmOrder() {
        String name = edtName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Bạn hãy điền đủ thông tin đã!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sau này chỗ này sẽ gọi API để tạo đơn hàng trong DB
        Toast.makeText(this, "Đặt hàng thành công bằng " + paymentMethod, Toast.LENGTH_LONG).show();
        finish();
    }
}