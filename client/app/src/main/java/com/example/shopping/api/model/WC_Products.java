package com.example.shopping.api.model;

import com.example.shopping.app.model.ProductTemp;

import java.util.List;


public class WC_Products {
    public List<ProductTemp> products;

    public WC_Products(List<ProductTemp> products) {
        this.products = products;
    }
}
