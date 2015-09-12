package com.example.shopping.app.model;

import java.util.List;


public class Attribute {
    public String name;
    public String slug;
    public String position;
    public boolean visible;
    public boolean variation;
    public String option;
    public List<String> options;

    public Attribute() {
        // nothing!
    }

    public Attribute(String name, String slug, String position, boolean visible, boolean variation, List<String> options) {
        this.name = name;
        this.slug = slug;
        this.position = position;
        this.visible = visible;
        this.variation = variation;
        this.options = options;
    }

    public Attribute(String name, String slug, String position, boolean visible, boolean variation, String option) {
        this.name = name;
        this.slug = slug;
        this.position = position;
        this.visible = visible;
        this.variation = variation;
        this.option = option;
    }
}
