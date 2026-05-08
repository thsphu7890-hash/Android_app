package com.example.hitcapp.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // Thay IP máy tính của ông vào đây (ví dụ: 192.168.1.5)
    // Không dùng localhost/127.0.0.1 vì Emulator không hiểu
    private static final String BASE_URL = "http://192.168.1.253:5000/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}