package com.example.hitcapp.Model;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("id") // ID của dòng trong bảng giỏ hàng
    private int id;

    @SerializedName("product_name")
    private String name;

    @SerializedName("price")
    private String price;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("image_url") // API sẽ trả về Link ảnh từ Server
    private String imageUrl;

    public CartItem(String name, String price, int quantity, String imageUrl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}