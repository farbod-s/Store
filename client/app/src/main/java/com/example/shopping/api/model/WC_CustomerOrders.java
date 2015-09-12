package com.example.shopping.api.model;

import com.example.shopping.app.model.OrderTemp;

import java.util.List;

public class WC_CustomerOrders {
    public List<OrderTemp> orders;

    public WC_CustomerOrders(List<OrderTemp> orders) {
        this.orders = orders;
    }
}
