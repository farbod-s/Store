package com.example.shopping.api.model;

import com.example.shopping.app.model.Order;


public class WC_Order {
    public Order order;

    public WC_Order() {
        // nothing!
    }

    public WC_Order(Order order) {
        this.order = order;
    }
}
