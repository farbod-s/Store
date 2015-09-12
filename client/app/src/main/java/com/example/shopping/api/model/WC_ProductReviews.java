package com.example.shopping.api.model;

import com.example.shopping.app.model.Review;

import java.util.List;


public class WC_ProductReviews {
    public List<Review> product_reviews;

    public WC_ProductReviews(List<Review> product_reviews) {
        this.product_reviews = product_reviews;
    }
}
