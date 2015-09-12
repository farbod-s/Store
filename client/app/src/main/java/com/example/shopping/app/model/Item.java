package com.example.shopping.app.model;

import java.util.ArrayList;
import java.util.List;


public class Item {
    public int id;
    public String subtotal;
    public String subtotal_tax;
    public String total;
    public String total_tax;
    public String price;
    public int quantity;
    public String tax_class;
    public String name;
    public int product_id;
    public String sku;
    public List<ItemMeta> meta = new ArrayList<>(); // Read-Only
    public List<OrderVariation> variations = new ArrayList<>(); // Write-Only
    public List<Attribute> attributes = new ArrayList<>(); // NULL
    public String featured_src; // NULL

    public Item() {
        // nothing!
    }

    // copy constructor
    public void update(Item item) {
        this.id = item.id;
        this.subtotal = item.subtotal;
        this.subtotal_tax = item.subtotal_tax;
        this.total = item.total;
        this.total_tax = item.total_tax;
        this.price = item.price;
        this.quantity = item.quantity;
        this.tax_class = item.tax_class;
        this.name = item.name;
        this.product_id = item.product_id;
        this.sku = item.sku;
        this.meta = item.meta;
        this.variations = item.variations;
        this.attributes = item.attributes;
        this.featured_src = item.featured_src;
    }

    public Item(int id, String subtotal, String subtotal_tax, String total, String total_tax, String price, int quantity, String tax_class, String name, int product_id, String sku, List<ItemMeta> meta) {
        this.id = id;
        this.subtotal = subtotal;
        this.subtotal_tax = subtotal_tax;
        this.total = total;
        this.total_tax = total_tax;
        this.price = price;
        this.quantity = quantity;
        this.tax_class = tax_class;
        this.name = name;
        this.product_id = product_id;
        this.sku = sku;
        this.meta = meta;
    }
}
