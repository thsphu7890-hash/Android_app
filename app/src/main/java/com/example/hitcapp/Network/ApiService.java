package com.example.hitcapp.Network;

import com.example.hitcapp.Model.CartItem;
import com.example.hitcapp.Model.Category;
import com.example.hitcapp.Model.Order;
import com.example.hitcapp.Model.Product;
import com.example.hitcapp.Model.User;
import com.example.hitcapp.Model.UserRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ==========================================
    // 1. AUTH & USER
    // ==========================================
    @POST("api/register")
    Call<User> register(@Body UserRequest userRequest);

    @POST("api/login")
    Call<User> login(@Body UserRequest loginRequest);

    // ==========================================
    // 2. DANH MỤC & SẢN PHẨM
    // ==========================================
    @GET("api/categories")
    Call<List<Category>> getCategories();

    @GET("api/products")
    Call<List<Product>> getAllProducts();

    @GET("api/products/search")
    Call<List<Product>> searchProducts(@Query("name") String productName);

    @GET("api/products/{id}")
    Call<Product> getProductDetail(@Path("id") int productId);

    // ==========================================
    // 3. GIỎ HÀNG
    // ==========================================
    @GET("api/cart/{userId}")
    Call<List<CartItem>> getCart(@Path("userId") int userId);

    @DELETE("api/cart/{cartId}")
    Call<Void> removeFromCart(@Path("cartId") int cartId);

    @FormUrlEncoded
    @POST("api/cart/add")
    Call<Void> addToCart(
            @Field("userId") int userId,     // ĐỂ LẠI userId ĐỂ KHỚP VỚI APP.JS
            @Field("productId") int productId, // ĐỂ LẠI productId ĐỂ KHỚP VỚI APP.JS
            @Field("quantity") int quantity
    );

    @FormUrlEncoded
    @PUT("api/cart/update/{cartId}")
    Call<Void> updateCartQuantity(
            @Path("cartId") int cartId,
            @Field("quantity") int quantity
    );

    // ==========================================
    // 4. ĐƠN HÀNG
    // ==========================================
    @GET("api/orders/user/{userId}")
    Call<List<Order>> getOrderHistory(@Path("userId") int userId);

    @POST("api/orders/create")
    Call<Order> createOrder(@Body Order order);
}