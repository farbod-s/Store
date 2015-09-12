package com.example.shopping.app.model;


public class Shipping {
    public int id;
    public String method_id;
    public String method_title;
    public float total;

    public Shipping() {
        // nothing!
    }

    public Shipping(int id, String method_id, String method_title, float total) {
        this.id = id;
        this.method_id = method_id;
        this.method_title = method_title;
        this.total = total;
    }
}
