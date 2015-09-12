package com.example.shopping.app.model;


public class Review {
    public int id;
    public String created_at;
    public String review;
    public String rating;
    public String reviewer_name;
    public String reviewer_email;
    public boolean verified;

    public Review() {
        // nothing!
    }

    public Review(int id, String created_at, String review, String rating, String reviewer_name, String reviewer_email, boolean verified) {
        this.id = id;
        this.created_at = created_at;
        this.review = review;
        this.rating = rating;
        this.reviewer_name = reviewer_name;
        this.reviewer_email = reviewer_email;
        this.verified = verified;
    }
}
