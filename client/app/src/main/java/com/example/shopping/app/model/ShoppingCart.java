package com.example.shopping.app.model;

import com.example.shopping.app.utility.Utils;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCart {
    private final static String tag = ShoppingCart.class.getSimpleName();

    public Order mOrder = new Order();

    public ShoppingCart() {
        mOrder.line_items = new ArrayList<>();
    }

    public boolean applyCoupon(Coupon coupon) {
        // TODO - apply coupon if possible
        // check individual usage
        // check product ids
        // check excluded product ids
        // check usage limit
        // check usage limit per user
        // check usage limit to items
        // check expiry date
        // check enable free shipping
        // check exclude sale items
        // check minimum and maximum amount
        // check customer emails

        if (coupon.usage_limit < coupon.usage_count) {
            if (coupon.minimum_amount < Float.valueOf(mOrder.subtotal) &&
                    coupon.maximum_amount > Float.valueOf(mOrder.subtotal)) {
                if (coupon.type.equals("percent")) {
                    final String customer_email = Prefs.getString(Utils.ACCOUNT_EMAIL_KEY, "");
                    if (coupon.customer_emails.contains(customer_email)) {
                        // TODO - add discount
                        return true;
                    }

                    for (Item item : mOrder.line_items) {
                        if (coupon.exclude_product_ids.contains(item.product_id)) {
                            return false;
                        } else if (coupon.product_ids.contains(item.product_id)) {
                            // TODO - check on-sale
                            // TODO - add discount
                            return true;
                        }
                    }
                } else if (coupon.type.equals("fixed-cart")) {
                    // TODO
                } // else { ... }
            }
        }

        return false;
    }

    public void updateOrderPrices() {
        float subtotal = 0.f, shipping = 0.f, tax = 0.f, discount = 0.f;
        int i = 0;
        for (Item item : mOrder.line_items) {
            subtotal += (Float.valueOf(item.price) * item.quantity);
            i++;
        }
        mOrder.subtotal = String.valueOf(subtotal);
        mOrder.total_shipping = String.valueOf(shipping); // FIXME
        mOrder.total_tax = String.valueOf(tax); // FIXME
        mOrder.total_discount = String.valueOf(discount); // FIXME
        mOrder.total = String.valueOf(subtotal + shipping + tax - discount);
        mOrder.total_line_items_quantity = String.valueOf(i);
    }

    public List<Item> getItems() {
        return mOrder.line_items;
    }

    public int getItemsCount() {
        return (mOrder.line_items != null) ? mOrder.line_items.size() : 0;
    }

    public boolean addItem(Item item) {
        // check item
        return updateItem(item) || mOrder.line_items.add(item);

    }

    public boolean updateItem(Item item) {
        int i = 0;
        for (Item itm : mOrder.line_items) {
            if (itm.product_id == item.product_id) { // item found
                mOrder.line_items.get(i).update(item);
                return true;
            }
            i++;
        }

        return false;
    }

    public boolean removeItem(Item item) {
        return mOrder.line_items.remove(item);
    }
}
