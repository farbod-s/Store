package com.example.shopping.app.model;


public class Image {
    public int id;
    public String created_at;
    public String updated_at;
    public String src;
    public String title;
    public String alt;
    public int position;

    public Image() {
        // nothing!
    }

    public Image(int id, String created_at, String updated_at, String src, String title, String alt, int position) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.src = src;
        this.title = title;
        this.alt = alt;
        this.position = position;
    }
}
