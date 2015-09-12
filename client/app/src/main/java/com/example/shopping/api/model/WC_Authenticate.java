package com.example.shopping.api.model;


public class WC_Authenticate {
    public String username;
    public String password;

    public WC_Authenticate() {
        // nothing!
    }

    public WC_Authenticate(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
