package com.example.shopping.app.model;


public class Customer {
    public int id;
    public String created_at;
    public String email;
    public String first_name;
    public String last_name;
    public String username;
    public String password; // Nullable
    public String role; // Nullable
    public int last_order_id;
    public String last_order_date;
    public int orders_count;
    public String total_spent;
    public String avatar_url;
    public BillingAddress billing_address;
    public ShippingAddress shipping_address;

    public Customer() {
        // nothing!
    }

    public Customer(int id, String created_at, String email, String first_name, String last_name, String username, int last_order_id, String last_order_date, int orders_count, String total_spent, String avatar_url, BillingAddress billing_address, ShippingAddress shipping_address) {
        this.id = id;
        this.created_at = created_at;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.last_order_id = last_order_id;
        this.last_order_date = last_order_date;
        this.orders_count = orders_count;
        this.total_spent = total_spent;
        this.avatar_url = avatar_url;
        this.billing_address = billing_address;
        this.shipping_address = shipping_address;
    }

    public Customer(int id, String created_at, String email, String first_name, String last_name, String username, String password, String role, int last_order_id, String last_order_date, int orders_count, String total_spent, String avatar_url, BillingAddress billing_address, ShippingAddress shipping_address) {
        this.id = id;
        this.created_at = created_at;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.last_order_id = last_order_id;
        this.last_order_date = last_order_date;
        this.orders_count = orders_count;
        this.total_spent = total_spent;
        this.avatar_url = avatar_url;
        this.billing_address = billing_address;
        this.shipping_address = shipping_address;
    }
}
