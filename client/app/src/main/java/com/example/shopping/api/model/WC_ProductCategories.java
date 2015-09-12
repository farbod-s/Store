package com.example.shopping.api.model;

import com.example.shopping.app.model.Category;

import java.util.List;


public class WC_ProductCategories {
    public List<Category> product_categories;

    public WC_ProductCategories(List<Category> product_categories) {
        this.product_categories = product_categories;
    }
}
