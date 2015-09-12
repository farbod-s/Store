package com.example.shopping.app.model;

import java.lang.reflect.Field;


public class OrderTemp {
    public int id;
    public int order_number;
    public String created_at;
    public String updated_at;
    public String completed_at;
    public String status;
    public String currency;
    public String total;
    public String subtotal;
    public String total_line_items_quantity;

    public OrderTemp() {
        // nothing!
    }

    public OrderTemp(int id, int order_number, String created_at, String updated_at, String completed_at, String status, String currency, String total, String subtotal, String total_line_items_quantity) {
        this.id = id;
        this.order_number = order_number;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.completed_at = completed_at;
        this.status = status;
        this.currency = currency;
        this.total = total;
        this.subtotal = subtotal;
        this.total_line_items_quantity = total_line_items_quantity;
    }

    public static String fields() {
        StringBuffer buffer = new StringBuffer();
        for (Field filed : OrderTemp.class.getDeclaredFields()) {
            if (buffer.length() > 0) {
                buffer.append(",");
            }
            buffer.append(filed.getName());
        }

        return buffer.toString();
    }
}
