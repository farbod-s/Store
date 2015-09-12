package com.example.shopping.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.utility.Utils;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ToolbarActivity extends AppCompatActivity {
    @Bind(R.id.btn_drawer) ImageView mDrawerButton;
    @Bind(R.id.btn_back) ImageView mBackButton;
    @Bind(R.id.toolbar_title) TextView mToolbarTitle;
    @Bind(R.id.btn_search) ImageView mSearchButton;
    @Bind(R.id.btn_shopping_cart) ImageView mShoppingCart;
    @Bind(R.id.text_view_shopping_cart_badge) TextView mShoppingCartBadge;
    @Bind(R.id.toolbar) View mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register event bus
        AppController.getInstance().getEventBus().register(this);

        if (mShoppingCart.getVisibility() == View.VISIBLE) {
            // update shopping cart badge
            int count = AppController.getInstance().getShoppingCart().getItemsCount();
            if (count > 0) {
                updateShoppingCartBadge(count);
                setShoppingCartBadgeVisibility(true);
            } else {
                setShoppingCartBadgeVisibility(false);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // unregister event bus
        AppController.getInstance().getEventBus().unregister(this);
    }

    public void bindViews() {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_back)
    public void toolbarBackHandler(View v) {
        onBackPressed();
    }

    @OnClick(R.id.btn_search)
    public void toolbarSearchHandler(View v) {
        Intent intent = new Intent(ToolbarActivity.this, OrderHistoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @OnClick(R.id.btn_shopping_cart)
    public void toolbarShoppingCartHandler(View v) {
        Intent intent = new Intent(ToolbarActivity.this, ShoppingCartActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    public void updateShoppingCartBadge(int count) {
        mShoppingCartBadge.setText(Utils.getStringValue(count));
    }

    public void setShoppingCartBadgeVisibility(boolean visible) {
        if (visible) {
            mShoppingCartBadge.setVisibility(View.VISIBLE);
        } else {
            mShoppingCartBadge.setVisibility(View.GONE);
        }
    }

    public void showToolbarDrawerButton() {
        mBackButton.setVisibility(View.GONE);
        mDrawerButton.setVisibility(View.VISIBLE);
    }

    public void showToolbarBackButton() {
        mDrawerButton.setVisibility(View.GONE);
        mBackButton.setVisibility(View.VISIBLE);
    }

    public void setToolbarSearchVisibility(boolean visible) {
        if (visible) {
            mSearchButton.setVisibility(View.VISIBLE);
        } else {
            mSearchButton.setVisibility(View.GONE);
        }
    }

    public void setToolbarShoppingCartVisibility(boolean visible) {
        if (visible) {
            mShoppingCart.setVisibility(View.VISIBLE);
        } else {
            mShoppingCart.setVisibility(View.GONE);
        }
        setShoppingCartBadgeVisibility(visible);
    }

    public void updateToolbarTitle(String title) {
        mToolbarTitle.setText(title);
    }

    public void updateToolbarTitle(int resId) {
        mToolbarTitle.setText(resId);
    }

    public void updateToolbarElevation(float elevation) {
        ViewCompat.setElevation(mToolbar, elevation);
    }
}
