package com.example.shopping.app.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.adapter.ShippingAddressAdapter;
import com.example.shopping.app.model.ShippingAddress;
import com.example.shopping.app.ui.dialog.AddShippingAddressDialog;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class ShippingAddressActivity extends ToolbarActivity {
    @Bind(R.id.scroll) RecyclerView mListView;
    @Bind(R.id.fab) FloatingActionButton mFab;
    @Bind(R.id.shipping_address_empty_message) TextView mEmptyMessage;

    private ShippingAddressAdapter mListAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<ShippingAddress> mShippingAddressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        // bind views
        bindViews();

        // update toolbar
        updateToolbarTitle(R.string.activity_shipping_address);
        showToolbarBackButton();
        setToolbarShoppingCartVisibility(false);
        setToolbarSearchVisibility(false);
        updateToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));

        this.mShippingAddressList = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLayoutManager);
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.setHasFixedSize(true);
        mListAdapter = new ShippingAddressAdapter();
        mListView.setAdapter(mListAdapter);

        mFab.attachToRecyclerView(mListView);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddShippingAddressDialog addAddressDialog = new AddShippingAddressDialog(ShippingAddressActivity.this);
                addAddressDialog.show();
            }
        });

        loadShippingAddresses();
    }

    @Subscribe
    public void onShippingAddressRemoved(ShippingAddress address) {
        if (!mFab.isVisible()) {
            mFab.show();
        }

        // TODO - remove from db
        if (mListAdapter.getItemCount() < 2) {
            mEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            mEmptyMessage.setVisibility(View.GONE);
        }
    }

    public void addShippingAddress(ShippingAddress address) {
        // TODO - add to db
        // add new address
        mListAdapter.add(mListAdapter.getItemCount(), address);
        mEmptyMessage.setVisibility(View.GONE);
    }

    private void loadShippingAddresses() {
        // TODO - retrieve from db
        if (mShippingAddressList.isEmpty()) {
            mEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            mEmptyMessage.setVisibility(View.GONE);
        }
    }
}
