package com.example.shopping.app.model;


public class Download {
    public String id; // file MD5 hash
    public String name;
    public String file;

    public Download() {
        // nothing!
    }

    public Download(String id, String name, String file) {
        this.id = id;
        this.name = name;
        this.file = file;
    }
}
