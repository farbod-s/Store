package com.example.shopping.app.model;


public class Dimension {
    public String length;
    public String width;
    public String height;
    public String unit;

    public Dimension() {
        // nothing!
    }

    public Dimension(String length, String width, String height, String unit) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.unit = unit;
    }
}
