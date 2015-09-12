package com.example.shopping.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.adapter.ItemAdapter;
import com.example.shopping.app.model.Item;
import com.example.shopping.app.model.ShoppingCartMessage;
import com.example.shopping.app.ui.dialog.AddCouponDialog;
import com.example.shopping.app.ui.dialog.EditInCartDialog;
import com.example.shopping.app.ui.dialog.RemoveFromCartDialog;
import com.example.shopping.app.ui.widget.ExpandableHeightListView;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class ShoppingCartActivity extends NavigationDrawerActivity {
    @Bind(R.id.items) ExpandableHeightListView mItemsListView;

    @Bind(R.id.cart_subtotal_price) TextView mSubtotalPrice;
    @Bind(R.id.cart_shipping_price) TextView mShippingPrice;
    @Bind(R.id.cart_tax_price) TextView mTaxPrice;
    @Bind(R.id.cart_discount_price) TextView mDiscountPrice;
    @Bind(R.id.cart_discount_layout) View mDiscountView;
    @Bind(R.id.cart_total_price) TextView mTotalPrice;

    @Bind(R.id.add_gift_note_layout) View mGiftNoteView;
    @Bind(R.id.gift_checkbox) CheckBox mGiftCheck;
    @Bind(R.id.gift_note_text) EditText mGiftNote;

    private ItemAdapter mItemsAdapter;

    private List<Item> mItems;

    //private EditInCartDialog mEditCartDialog = null;
    //private RemoveFromCartDialog mRemoveCartDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // inject views
        bindViews();

        // update toolbar
        updateToolbarTitle(R.string.activity_shopping_cart_title);
        showToolbarDrawerButton();
        setToolbarShoppingCartVisibility(false);
        setToolbarSearchVisibility(false);
        updateToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));

        // build drawer layout
        buildDrawerLayout(savedInstanceState);

        // update navigation drawer
        updateDrawerItemSelection(DRAWER_ITEM_SHOPPING_CART_ID);

        // items
        mItems = new ArrayList<>();
        mItemsAdapter = new ItemAdapter(this, mItems);
        mItemsListView.setExpanded(true);
        mItemsListView.setAdapter(mItemsAdapter);

        updateItems();

        mGiftNoteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGiftCheck.performClick();
            }
        });

        mGiftCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if (check) {
                    mGiftNote.setVisibility(View.VISIBLE);
                } else {
                    mGiftNote.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateItems() {
        mItems.clear();
        mItems.addAll(AppController.getInstance().getShoppingCart().getItems());
        mItemsAdapter.notifyDataSetChanged();

        updatePrices();
    }

    private void updatePrices() {
        AppController.getInstance().getShoppingCart().updateOrderPrices();

        mSubtotalPrice.setText(AppController.getInstance().getShoppingCart().mOrder.subtotal);
        mShippingPrice.setText(AppController.getInstance().getShoppingCart().mOrder.total_shipping);
        mTaxPrice.setText(AppController.getInstance().getShoppingCart().mOrder.total_tax);
        mDiscountPrice.setText(AppController.getInstance().getShoppingCart().mOrder.total_discount);

        float discount = Float.valueOf(AppController.getInstance().getShoppingCart().mOrder.total_discount);
        if (discount > 0.f) {
            mDiscountView.setVisibility(View.VISIBLE);
        } else {
            mDiscountView.setVisibility(View.GONE);
        }

        mTotalPrice.setText(AppController.getInstance().getShoppingCart().mOrder.total);
    }

    @Subscribe
    public void doActionOnShoppingCartItem(ShoppingCartMessage message) {
        if (message.action.equals("remove")) {  // remove
            RemoveFromCartDialog removeCartDialog = new RemoveFromCartDialog(this, message.item);
            removeCartDialog.show();
        } else if (message.action.equals("edit")) { // edit
            EditInCartDialog editCartDialog = new EditInCartDialog(this, message.item);
            editCartDialog.show();
        }
    }

    public void onItemEdited(Item item) {
        AppController.getInstance().getShoppingCart().updateItem(item);
        mItemsAdapter.notifyDataSetChanged();

        updatePrices();
    }

    public void onItemRemoved(Item item) {
        AppController.getInstance().getShoppingCart().removeItem(item);

        updateItems();

        // update shopping cart badge
        int count = AppController.getInstance().getShoppingCart().getItemsCount();
        if (count > 0) {
            updateDrawerItemBadge(DRAWER_ITEM_SHOPPING_CART_ID, count);
        }
    }

    public void onCouponApplied() {
        // update prices
        updatePrices();
    }

    @OnClick(R.id.add_coupon_layout)
    public void onAddCouponClicked() {
        AddCouponDialog couponDialog = new AddCouponDialog(this);
        couponDialog.show();
    }

    @OnClick(R.id.cart_checkout_button)
    public void checkout() {
        Intent intent = new Intent(ShoppingCartActivity.this, CheckoutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
