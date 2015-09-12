package com.example.shopping.app.model;

import java.lang.reflect.Field;


public class ProductTemp {
    public int id;
    public String title;
    public float price;
    public float regular_price;
    public float sale_price;
    public String price_html;
    public boolean on_sale;
    public String featured_src;

    public ProductTemp() {
        // nothing!
    }

    public ProductTemp(int id, String title, float price, float regular_price, float sale_price, String price_html, boolean on_sale, String featured_src) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.regular_price = regular_price;
        this.sale_price = sale_price;
        this.price_html = price_html;
        this.on_sale = on_sale;
        this.featured_src = featured_src;
    }

    public static String fields() {
        StringBuffer buffer = new StringBuffer();
        for (Field filed : ProductTemp.class.getDeclaredFields()) {
            if (buffer.length() > 0) {
                buffer.append(",");
            }
            buffer.append(filed.getName());
        }

        return buffer.toString();
    }
}
