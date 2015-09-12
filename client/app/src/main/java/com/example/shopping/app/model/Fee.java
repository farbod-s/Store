package com.example.shopping.app.model;


public class Fee {
    public int id;
    public String title;
    public boolean taxable;
    public String tax_class;
    public float total;
    public float total_tax;

    public Fee() {
        // nothing!
    }

    public Fee(int id, String title, boolean taxable, String tax_class, float total, float total_tax) {
        this.id = id;
        this.title = title;
        this.taxable = taxable;
        this.tax_class = tax_class;
        this.total = total;
        this.total_tax = total_tax;
    }
}
