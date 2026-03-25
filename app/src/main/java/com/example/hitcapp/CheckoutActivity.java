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

        // Nhận dữ liệu tổng tiền
        String totalPrice = getIntent().getStringExtra("total_price");
        if (totalPrice != null) {
            tvTotal.setText(totalPrice);
        }

        // Xử lý chọn thanh toán -> Hiện QR
        rgPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_cod) {
                paymentMethod = "COD";
            } else if (checkedId == R.id.rb_bank) {
                paymentMethod = "Chuyển khoản";
                showQRCodeDialog();
            }
        });

        // Nút Hủy
        btnCancel.setOnClickListener(v -> {
            Toast.makeText(this, "Đã hủy giao dịch", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Nút Xác nhận
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

        String amount = tvTotal.getText().toString().replace(".", "").replace("đ", "").trim();
        // MB Bank của Phú (Thay số tài khoản vào đây)
        String qrUrl = "https://img.vietqr.io/image/mbbank-123456789-compact.png?amount=" + amount + "&addInfo=HitCApp Pay";

        Glide.with(this).load(qrUrl).into(imgQR);
        tvContent.setText("Số tiền: " + tvTotal.getText().toString());

        btnDone.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void confirmOrder() {
        if (edtName.getText().toString().isEmpty() || edtAddress.getText().toString().isEmpty()) {
            Toast.makeText(this, "Phú ơi, điền đủ thông tin đã!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Đặt hàng thành công bằng " + paymentMethod, Toast.LENGTH_LONG).show();
        finish();
    }
}