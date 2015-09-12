package com.example.shopping.app.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.api.model.WC_Order;
import com.example.shopping.app.AppController;
import com.example.shopping.app.utility.Utils;
import com.example.shopping.app.adapter.ItemCheckoutAdapter;
import com.example.shopping.app.model.Item;
import com.example.shopping.app.model.Order;
import com.example.shopping.app.ui.widget.ExpandableHeightListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class OrderActivity extends ToolbarActivity {
    @Bind(R.id.scroll) View mContentView;
    @Bind(R.id.items) ExpandableHeightListView mItemsListView;
    @Bind(R.id.load_progress) ProgressBar mLoadProgress;
    @Bind(R.id.load_error_layout) View mLoadErrorView;

    @Bind(R.id.order_count) TextView mOrderCount;
    @Bind(R.id.order_status) TextView mOrderStatus;
    @Bind(R.id.order_date) TextView mOrderDate;
    @Bind(R.id.order_payment) TextView mOrderPaymentMethod;

    @Bind(R.id.order_name) TextView mOrderName;
    @Bind(R.id.order_province) TextView mShippingAddressProvince;
    @Bind(R.id.order_city) TextView mShippingAddressCity;
    @Bind(R.id.order_address) TextView mShippingAddress;
    @Bind(R.id.order_postalCode) TextView mShippingAddressPostalCode;
    @Bind(R.id.order_emergencyPhone) TextView mShippingAddressEmergencyPhone;
    @Bind(R.id.order_phone) TextView mShippingAddressPhone;

    @Bind(R.id.order_subtotal_price) TextView mSubtotalPrice;
    @Bind(R.id.order_discount_layout) View mDiscountView;
    @Bind(R.id.order_discount_price) TextView mDiscountPrice;
    @Bind(R.id.order_shipping_price) TextView mShippingPrice;
    @Bind(R.id.order_tax_price) TextView mTaxPrice;
    @Bind(R.id.order_total_price) TextView mTotalPrice;

    private Order mOrder = null;

    private int mOrderId;

    private ItemCheckoutAdapter mItemsAdapter;

    private List<Item> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // bind views
        bindViews();

        // update toolbar
        updateToolbarTitle(R.string.activity_order_title);
        showToolbarBackButton();
        setToolbarShoppingCartVisibility(false);
        setToolbarSearchVisibility(false);
        updateToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));

        // preprocess
        mOrderId = getIntent().getIntExtra(Utils.INTENT_ORDER_KEY, -1);
        // preprocess
        mContentView.setVisibility(View.GONE);
        mLoadProgress.setVisibility(View.VISIBLE);

        // items
        mItems = new ArrayList<>();
        mItemsAdapter = new ItemCheckoutAdapter(this, mItems);
        mItemsListView.setExpanded(true);
        mItemsListView.setAdapter(mItemsAdapter);

        // get data from server
        loadDataFromApi();
    }

    @OnClick(R.id.load_retry_button)
    public void retryLoadDataFromApi() {
        // preprocess
        mLoadErrorView.setVisibility(View.GONE);
        mLoadProgress.setVisibility(View.VISIBLE);

        loadDataFromApi();
    }

    private void loadDataFromApi() {
        AppController.getInstance().getRestClient().getApiService().getOrder(
                mOrderId,
                new Callback<WC_Order>() {
                    @Override
                    public void success(WC_Order order, Response response) {
                        mOrder = order.order;
                        updateItems();

                        // postprocess
                        mLoadProgress.setVisibility(View.GONE);
                        mContentView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // postprocess
                        mLoadProgress.setVisibility(View.GONE);
                        mLoadErrorView.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    private void updateItems() {
        mItems.clear();
        mItems.addAll(mOrder.line_items);
        mItemsAdapter.notifyDataSetChanged();

        updateDetails();
        updateShipping();
        updatePrices();
    }

    private void updateDetails() {
        mOrderCount.setText(String.valueOf(mOrder.order_number));
        mOrderStatus.setText(mOrder.status);
        mOrderDate.setText(mOrder.created_at);
        mOrderPaymentMethod.setText(mOrder.payment_details.method_title);
    }

    private void updateShipping() {
        mOrderName.setText(mOrder.shipping_address.first_name + " " + mOrder.shipping_address.last_name);
        mShippingAddressProvince.setText(mOrder.shipping_address.state);
        mShippingAddressCity.setText(mOrder.shipping_address.city);
        mShippingAddress.setText(mOrder.shipping_address.address_1 + " " + mOrder.shipping_address.address_2);
        mShippingAddressPostalCode.setText(mOrder.shipping_address.postcode);
        mShippingAddressEmergencyPhone.setText("");
        mShippingAddressPhone.setText("");
    }

    private void updatePrices() {
        mSubtotalPrice.setText(mOrder.subtotal);
        mShippingPrice.setText(mOrder.total_shipping);
        mTaxPrice.setText(mOrder.total_tax);
        mDiscountPrice.setText(mOrder.total_discount);

        float discount = Float.valueOf(mOrder.total_discount);
        if (discount > 0.f) {
            mDiscountView.setVisibility(View.VISIBLE);
        } else {
            mDiscountView.setVisibility(View.GONE);
        }

        mTotalPrice.setText(mOrder.total);
    }
}
