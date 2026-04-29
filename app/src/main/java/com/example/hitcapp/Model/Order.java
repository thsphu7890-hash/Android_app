package com.example.hitcapp.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Order {
    @SerializedName("id")
    private int id;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("total_price")
    private String totalPrice;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("order_date")
    private String orderDate;

    @SerializedName("status") // Ví dụ: "Pending", "Success", "Cancelled"
    private String status;

    // Nếu ông muốn lấy cả danh sách sản phẩm trong đơn hàng đó
    @SerializedName("items")
    private List<CartItem> items;

    // Constructor mặc định cho GSON
    public Order() {
    }

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}