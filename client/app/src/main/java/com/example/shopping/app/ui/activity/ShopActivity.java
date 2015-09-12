package com.example.shopping.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.shopping.R;
import com.example.shopping.app.utility.Utils;
import com.example.shopping.app.adapter.ShopTabsPagerAdapter;
import com.example.shopping.app.model.Category;
import com.example.shopping.app.model.Collection;
import com.example.shopping.app.model.ProductTemp;
import com.example.shopping.app.ui.widget.SlidingTabLayout;

import com.squareup.otto.Subscribe;

import butterknife.Bind;


public class ShopActivity extends NavigationDrawerActivity {
    @Bind(R.id.header) View mHeaderView;
    @Bind(R.id.pager) ViewPager mPager;

    private ShopTabsPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // bind views
        bindViews();

        // update toolbar
        updateToolbarTitle(R.string.app_name);
        showToolbarDrawerButton();
        updateToolbarElevation(.0f);

        // build drawer layout
        buildDrawerLayout(savedInstanceState);

        // update navigation drawer
        updateDrawerItemSelection(DRAWER_ITEM_SHOP_ID);

        // init pager
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        mPagerAdapter = new ShopTabsPagerAdapter(getSupportFragmentManager(), this);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mPagerAdapter.getCount()); // scroll to first item
        mPager.setOffscreenPageLimit(4);

        // init tabs
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, R.id.tab_indicator_title);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.md_white_1000));
        slidingTabLayout.setViewPager(mPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public ViewPager getViewPager() {
        return this.mPager;
    }

    @Subscribe
    public void onCategoryClicked(Category category) {
        Intent intent = new Intent(ShopActivity.this, ProductListActivity.class);
        intent.putExtra(Utils.INTENT_TYPE_KEY, Utils.INTENT__CATEGORY_KEY);
        intent.putExtra(Utils.INTENT_CATEGORY_NAME_KEY, category.slug); // FIXME - put category into bundle
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Subscribe
    public void onProductClicked(ProductTemp product) {
        Intent intent = new Intent(ShopActivity.this, ProductActivity.class);
        intent.putExtra(Utils.INTENT_PRODUCT_KEY, product.id);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Subscribe
    public void onCollectionClicked(Collection collection) {
        Intent intent = new Intent(ShopActivity.this, ProductListActivity.class);
        intent.putExtra(Utils.INTENT_TYPE_KEY, Utils.INTENT__COLLECTION_KEY);
        // TODO - put collection into bundle
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
