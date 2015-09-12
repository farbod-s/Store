package com.example.shopping.app.model;


public class Category {
    public int id;
    public String name;
    public String slug;
    public int parent;
    public String description;
    public int count;

    public Category() {
        // nothing!
    }

    public Category(int id, String name, String slug, int parent, String description, int count) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.parent = parent;
        this.description = description;
        this.count = count;
    }
}