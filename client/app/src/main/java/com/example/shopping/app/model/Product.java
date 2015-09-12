package com.example.shopping.app.model;

import java.lang.reflect.Field;
import java.util.List;


public class Product {
    public String title;
    public int id;
    public String sku;
    public float price;
    public float regular_price;
    public float sale_price;
    public String price_html;
    public int stock_quantity;
    public boolean in_stock;
    public boolean purchaseable;
    public boolean featured;
    public boolean visible;
    public String catalog_visibility;
    public boolean on_sale;
    public String weight;
    public String shipping_class;
    public int shipping_class_id;
    public String description;
    public String short_description;
    public boolean reviews_allowed;
    public String average_rating;
    public int rating_count;
    public List<Integer> related_ids;
    public int parent_id;
    public List<String> categories;
    public List<String> tags;
    public List<Image> images;
    public String featured_src;
    public List<Attribute> attributes;
    public List<Download> downloads;
    public String purchase_note;
    public int total_sales;

    public Product() {
        // nothing!
    }

    public Product(String title, int id, String sku, float price, float regular_price, float sale_price, String price_html, int stock_quantity, boolean in_stock, boolean purchaseable, boolean featured, boolean visible, String catalog_visibility, boolean on_sale, String weight, String shipping_class, int shipping_class_id, String description, String short_description, boolean reviews_allowed, String average_rating, int rating_count, List<Integer> related_ids, int parent_id, List<String> categories, List<String> tags, List<Image> images, String featured_src, List<Attribute> attributes, List<Download> downloads, String purchase_note, int total_sales) {
        this.title = title;
        this.id = id;
        this.sku = sku;
        this.price = price;
        this.regular_price = regular_price;
        this.sale_price = sale_price;
        this.price_html = price_html;
        this.stock_quantity = stock_quantity;
        this.in_stock = in_stock;
        this.purchaseable = purchaseable;
        this.featured = featured;
        this.visible = visible;
        this.catalog_visibility = catalog_visibility;
        this.on_sale = on_sale;
        this.weight = weight;
        this.shipping_class = shipping_class;
        this.shipping_class_id = shipping_class_id;
        this.description = description;
        this.short_description = short_description;
        this.reviews_allowed = reviews_allowed;
        this.average_rating = average_rating;
        this.rating_count = rating_count;
        this.related_ids = related_ids;
        this.parent_id = parent_id;
        this.categories = categories;
        this.tags = tags;
        this.images = images;
        this.featured_src = featured_src;
        this.attributes = attributes;
        this.downloads = downloads;
        this.purchase_note = purchase_note;
        this.total_sales = total_sales;
    }

    public static String fields() {
        StringBuffer buffer = new StringBuffer();
        for (Field filed : Product.class.getDeclaredFields()) {
            if (buffer.length() > 0) {
                buffer.append(",");
            }
            buffer.append(filed.getName());
        }

        return buffer.toString();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (Field filed : this.getClass().getDeclaredFields()) {
            if (buffer.length() > 0) {
                buffer.append(",");
            }
            buffer.append(filed.getName());
        }

        return buffer.toString();
    }
}
