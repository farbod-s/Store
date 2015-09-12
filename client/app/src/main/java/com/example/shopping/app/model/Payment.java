package com.example.shopping.app.model;


public class Payment {
    public String method_id;
    public String method_title;
    public boolean paid;
    public String transaction_id;

    public Payment() {
        // nothing!
    }

    public Payment(String method_id, String method_title, boolean paid, String transaction_id) {
        this.method_id = method_id;
        this.method_title = method_title;
        this.paid = paid;
        this.transaction_id = transaction_id;
    }
}
