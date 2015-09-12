package com.example.shopping.api.model;

import com.example.shopping.app.model.Customer;


public class WC_Customer {
    public Customer customer;

    public WC_Customer() {
        // nothing!
    }

    public WC_Customer(Customer customer) {
        this.customer = customer;
    }
}
