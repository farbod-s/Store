package com.example.shopping.app.model;

import java.util.List;


public class Coupon {
    public int id;
    public String code;
    public String type;
    public float amount;
    public boolean individual_use;
    public List<Integer> product_ids;
    public List<Integer> exclude_product_ids;
    public int usage_limit;
    public int usage_limit_per_user;
    public int limit_usage_to_x_items;
    public int usage_count;
    public String expiry_date;
    public boolean enable_free_shipping;
    public List<Integer> product_category_ids;
    public List<Integer> exclude_product_category_ids;
    public boolean exclude_sale_items;
    public float minimum_amount;
    public float maximum_amount;
    public List<String> customer_emails;
    public String description;

    public Coupon() {
        // nothing!
    }

    public Coupon(int id, String code, String type, float amount, boolean individual_use, List<Integer> product_ids, List<Integer> exclude_product_ids, int usage_limit, int usage_limit_per_user, int limit_usage_to_x_items, int usage_count, String expiry_date, boolean enable_free_shipping, List<Integer> product_category_ids, List<Integer> exclude_product_category_ids, boolean exclude_sale_items, float minimum_amount, float maximum_amount, List<String> customer_emails, String description) {
        this.id = id;
        this.code = code;
        this.type = type;
        this.amount = amount;
        this.individual_use = individual_use;
        this.product_ids = product_ids;
        this.exclude_product_ids = exclude_product_ids;
        this.usage_limit = usage_limit;
        this.usage_limit_per_user = usage_limit_per_user;
        this.limit_usage_to_x_items = limit_usage_to_x_items;
        this.usage_count = usage_count;
        this.expiry_date = expiry_date;
        this.enable_free_shipping = enable_free_shipping;
        this.product_category_ids = product_category_ids;
        this.exclude_product_category_ids = exclude_product_category_ids;
        this.exclude_sale_items = exclude_sale_items;
        this.minimum_amount = minimum_amount;
        this.maximum_amount = maximum_amount;
        this.customer_emails = customer_emails;
        this.description = description;
    }
}
