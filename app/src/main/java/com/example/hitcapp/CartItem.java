package com.example.hitcapp;

public class CartItem {
    private String name;
    private String price;
    private int quantity;
    private int imageRes; // ID của ảnh trong thư mục drawable

    // Constructor để khởi tạo một đôi giày trong giỏ hàng
    public CartItem(String name, String price, int quantity, int imageRes) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageRes = imageRes;
    }

    // Mấy cái Getter/Setter này để thằng Adapter nó lấy dữ liệu ra xài
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
}