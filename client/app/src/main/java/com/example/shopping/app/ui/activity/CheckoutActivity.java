package com.example.shopping.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.adapter.ItemCheckoutAdapter;
import com.example.shopping.app.model.Item;
import com.example.shopping.app.model.ShippingAddress;
import com.example.shopping.app.ui.widget.ExpandableHeightListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class CheckoutActivity extends ToolbarActivity {
    @Bind(R.id.items) ExpandableHeightListView mItemsListView;

    @Bind(R.id.shipping_address_text) TextView mShippingAddressText;

    @Bind(R.id.shipping_class_one_button) RadioButton mShippingClassOneButton;
    @Bind(R.id.shipping_class_one_layout) View mShippingClassOneView;
    @Bind(R.id.shipping_class_two_button) RadioButton mShippingClassTwoButton;
    @Bind(R.id.shipping_class_two_layout) View mShippingClassTwoView;

    @Bind(R.id.payment_one_button) RadioButton mPaymentOneButton;
    @Bind(R.id.payment_one_layout) View mPaymentOneView;
    @Bind(R.id.payment_two_button) RadioButton mPaymentTwoButton;
    @Bind(R.id.payment_two_layout) View mPaymentTwoView;

    @Bind(R.id.cart_subtotal_price) TextView mSubtotalPrice;
    @Bind(R.id.cart_shipping_price) TextView mShippingPrice;
    @Bind(R.id.cart_tax_price) TextView mTaxPrice;
    @Bind(R.id.cart_discount_price) TextView mDiscountPrice;
    @Bind(R.id.cart_discount_layout) View mDiscountView;
    @Bind(R.id.cart_total_price) TextView mTotalPrice;

    @Bind(R.id.add_shipping_address_layout) View mAddShippingAddressView;

    private ItemCheckoutAdapter mItemsAdapter;

    private List<Item> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // bind views
        bindViews();

        // update toolbar
        updateToolbarTitle(R.string.activity_shopping_cart_review);
        showToolbarBackButton();
        setToolbarShoppingCartVisibility(false);
        setToolbarSearchVisibility(false);
        updateToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));

        // init radio button
        makeRadioButtonsGrouped();

        // items
        mItems = new ArrayList<>();
        mItemsAdapter = new ItemCheckoutAdapter(this, mItems);
        mItemsListView.setExpanded(true);
        mItemsListView.setAdapter(mItemsAdapter);

        updateItems();

        mAddShippingAddressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActivity.this, ShippingAddressActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        mShippingAddressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddShippingAddressView.performClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final ShippingAddress address = AppController.getInstance().getShoppingCart().mOrder.shipping_address;
        if (address != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(address.first_name)
                    .append(" ")
                    .append(address.last_name)
                    .append(" / ")
                    .append(address.state)
                    .append(" / ")
                    .append(address.city)
                    .append(" / ")
                    .append(address.address_1)
                    .append(" / ")
                    .append(address.postcode);
            mShippingAddressText.setText(sb.toString());
        }

        if (!mShippingAddressText.getText().toString().isEmpty()) {
            mShippingAddressText.setVisibility(View.VISIBLE);
        } else {
            mShippingAddressText.setVisibility(View.GONE);
        }
    }

    private void makeRadioButtonsGrouped() {
        mShippingClassOneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShippingClassOneButton.performClick();
            }
        });

        mShippingClassTwoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShippingClassTwoButton.performClick();
            }
        });

        mShippingClassOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShippingClassOneButton.setChecked(true);
                mShippingClassTwoButton.setChecked(false);
            }
        });

        mShippingClassTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShippingClassOneButton.setChecked(false);
                mShippingClassTwoButton.setChecked(true);
            }
        });

        mPaymentOneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPaymentOneButton.performClick();
            }
        });

        mPaymentTwoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPaymentTwoButton.performClick();
            }
        });

        mPaymentOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPaymentOneButton.setChecked(true);
                mPaymentTwoButton.setChecked(false);
            }
        });

        mPaymentTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPaymentOneButton.setChecked(false);
                mPaymentTwoButton.setChecked(true);
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
}
