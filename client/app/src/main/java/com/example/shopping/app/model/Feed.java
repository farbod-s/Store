package com.example.shopping.app.model;


public class Feed {
    public int id = -1;
    public String title;
    public String imageUrl;
    public String description;
    public String datetime;
    public int comments;

    public Feed() {
        // nothing!
    }

    public Feed(String title, String imageUrl, String description, String datetime, int comments) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.datetime = datetime;
        this.comments = comments;
    }
}
