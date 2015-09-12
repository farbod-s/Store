package com.example.shopping.app.model;

import java.util.List;


public class Order {
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
    public String total_tax;
    public String total_shipping;
    public String cart_tax;
    public String shipping_tax;
    public String total_discount;
    public String shipping_methods;
    public Payment payment_details;
    public BillingAddress billing_address;
    public ShippingAddress shipping_address;
    public String note;
    public int customer_id;
    public List<Item> line_items;
    public List<Shipping> shipping_lines;
    public List<Tax> tax_lines;
    public List<Fee> fee_lines;
    public List<Coupon> coupon_lines;

    public Order() {
        // nothing!
    }

    public Order(int id, int order_number, String created_at, String updated_at, String completed_at, String status, String currency, String total, String subtotal, String total_line_items_quantity, String total_tax, String total_shipping, String cart_tax, String shipping_tax, String total_discount, String shipping_methods, Payment payment_details, BillingAddress billing_address, ShippingAddress shipping_address, String note, int customer_id, List<Item> line_items) {
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
        this.total_tax = total_tax;
        this.total_shipping = total_shipping;
        this.cart_tax = cart_tax;
        this.shipping_tax = shipping_tax;
        this.total_discount = total_discount;
        this.shipping_methods = shipping_methods;
        this.payment_details = payment_details;
        this.billing_address = billing_address;
        this.shipping_address = shipping_address;
        this.note = note;
        this.customer_id = customer_id;
        this.line_items = line_items;
    }
}
