package com.example.hitcapp.Model;

import java.io.Serializable;

/**
 * Class User đại diện cho thông tin người dùng.
 * Triển khai Serializable để có thể truyền object qua Intent hoặc Bundle.
 */
public class User implements Serializable {
    // Nên có serialVersionUID để đảm bảo tính tương thích khi truyền nhận object
    private static final long serialVersionUID = 1L;

    private int id; // Nếu Backend của ông có trả về ID
    private String username;
    private String email;
    private String password;
    private String phoneNumber; // Nếu có
    private String avatar;      // Nếu có

    // 1. Constructor mặc định (Bắt buộc cho một số thư viện như Gson)
    public User() {
    }

    // 2. Constructor đầy đủ tham số
    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // 3. Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Ghi đè phương thức toString để dễ debug log
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}