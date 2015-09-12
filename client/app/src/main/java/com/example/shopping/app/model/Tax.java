package com.example.shopping.app.model;


public class Tax {
    public int id;
    public int rate_id;
    public String code;
    public String title;
    public float total;
    public boolean compound;

    public Tax() {
        // nothing!
    }

    public Tax(int id, int rate_id, String code, String title, float total, boolean compound) {
        this.id = id;
        this.rate_id = rate_id;
        this.code = code;
        this.title = title;
        this.total = total;
        this.compound = compound;
    }
}
