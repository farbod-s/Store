package com.example.shopping.app.model;


public class ShoppingCartMessage {
    public Item item;
    public String action; // TRUE=remove, FALSE=edit

    public ShoppingCartMessage() {
        // nothing!
    }

    public ShoppingCartMessage(Item item, String action) {
        this.item = item;
        this.action = action;
    }
}
