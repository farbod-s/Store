package com.example.shopping.app.model;


public class Collection {
    public int id = -1;
    public String title;
    public String imageCover;
    public String imageMore1;
    public String imageMore2;
    public String imageMore3;
    public String imageMore4;

    public Collection() {
        // nothing!
    }

    public Collection(String title, String imageCover, String imageMore1, String imageMore2, String imageMore3, String imageMore4) {
        this.title = title;
        this.imageCover = imageCover;
        this.imageMore1 = imageMore1;
        this.imageMore2 = imageMore2;
        this.imageMore3 = imageMore3;
        this.imageMore4 = imageMore4;
    }
}
