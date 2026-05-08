package com.example.hitcapp.Model;

import com.google.gson.annotations.SerializedName;

public class Product {
    private int id;
    private String name;
    private String price;
    private String description;

    @SerializedName("image_url")
    private String imageUrl;

    // BỔ SUNG BIẾN NÀY ĐỂ LẤY TÊN HÃNG
    @SerializedName("category_name")
    private String categoryName;

    // --- Getters ---
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }

    // Getter cho tên hãng
    public String getCategoryName() { return categoryName; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(String price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}