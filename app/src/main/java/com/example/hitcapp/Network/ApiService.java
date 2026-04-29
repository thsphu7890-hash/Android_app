package com.example.hitcapp.Network;

import com.example.hitcapp.Model.CartItem; // Phú nhớ thêm dòng này để hết lỗi đỏ CartItem
import com.example.hitcapp.Model.Category;
import com.example.hitcapp.Model.Order;
import com.example.hitcapp.Model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE; // Thêm cái này để dùng được @DELETE
import retrofit2.http.GET;
import retrofit2.http.POST; // Thêm cái này nếu ông muốn làm nút "Thêm vào giỏ"
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // --- DANH MỤC (CATEGORY) ---
    @GET("api/categories")
    Call<List<Category>> getCategories();


    // --- SẢN PHẨM ---
    @GET("api/products")
    Call<List<Product>> getAllProducts();

    @GET("api/products/hot")
    Call<List<Product>> getHotProducts();

    @GET("api/products/search")
    Call<List<Product>> searchProducts(@Query("name") String productName);

    @GET("api/products/{id}")
    Call<Product> getProductDetail(@Path("id") int productId);

    @GET("api/products/category/{categoryId}")
    Call<List<Product>> getProductsByCategory(@Path("categoryId") int categoryId);


    // --- GIỎ HÀNG (CART) ---

    // Lấy giỏ hàng theo User
    @GET("api/cart/{userId}")
    Call<List<CartItem>> getCart(@Path("userId") int userId);

    // Xóa item khỏi giỏ (Phải có import retrofit2.http.DELETE)
    @DELETE("api/cart/{cartId}")
    Call<Void> removeFromCart(@Path("cartId") int cartId);

    // Thêm sản phẩm vào giỏ (Dùng cho nút "Thêm vào giỏ" ở trang Detail)
    @POST("api/cart/add")
    Call<Void> addToCart(@Query("userId") int userId, @Query("productId") int productId, @Query("quantity") int quantity);

    // Lấy danh sách các đơn hàng mà User đó đã đặt
    @GET("api/orders/user/{userId}")
    Call<List<Order>> getOrderHistory(@Path("userId") int userId);
}