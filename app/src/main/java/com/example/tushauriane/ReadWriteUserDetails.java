package com.example.tushauriane;


public class ReadWriteUserDetails {
    public String email, username;

    public ReadWriteUserDetails() {

    }

    public ReadWriteUserDetails(String email, String username){
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

