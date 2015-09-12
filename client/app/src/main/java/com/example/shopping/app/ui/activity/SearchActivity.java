package com.example.shopping.app.ui.activity;

import android.os.Bundle;

import com.example.shopping.R;


public class SearchActivity extends ToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // bind views
        bindViews();

        // update toolbar
        updateToolbarTitle(R.string.activity_search_title);
        showToolbarBackButton();
        setToolbarShoppingCartVisibility(false);
        setToolbarSearchVisibility(false);
        updateToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
    }
}