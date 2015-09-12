package com.example.shopping.app.model;


public class ItemMeta {
    public String key;
    public String label;
    public String value;

    public ItemMeta() {
        // nothing!
    }

    public ItemMeta(String key, String label, String value) {
        this.key = key;
        this.label = label;
        this.value = value;
    }
}
