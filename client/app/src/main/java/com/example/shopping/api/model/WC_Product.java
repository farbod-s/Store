package com.example.shopping.api.model;

import com.example.shopping.app.model.Product;


public class WC_Product {
    public Product product;

    public WC_Product() {
        // nothing!
    }

    public WC_Product(Product product) {
        this.product = product;
    }
}
