package com.example.hitcapp.Model;

import com.google.gson.annotations.SerializedName;

public class Product {
    private int id; // ID thường là kiểu số nguyên
    private String name;
    private String price;
    private String description; // Cần thêm biến này để hiện ở trang chi tiết

    @SerializedName("image_url")
    private String imageUrl;

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    // --- Setters (Nếu cần dùng để gán dữ liệu thủ công) ---
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(String price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
}