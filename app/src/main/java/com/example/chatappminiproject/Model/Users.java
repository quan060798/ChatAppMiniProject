package com.example.chatappminiproject.Model;

public class Users {

    private String id;
    private String username;
    private String imageURL;
    private String phone;
    private String email;


    public Users(String id, String username, String imageURL, String phone, String email) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.phone = phone;
        this.email = email;
    }

    public Users() {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageURL;
    }

    public void setImageUrl(String imageURL) {
        this.imageURL = imageURL;
    }
}
