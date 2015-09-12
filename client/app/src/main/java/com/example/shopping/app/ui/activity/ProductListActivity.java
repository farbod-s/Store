package com.example.shopping.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.shopping.R;
import com.example.shopping.api.model.WC_Products;
import com.example.shopping.app.AppController;
import com.example.shopping.app.ui.widget.GridEndlessScrollListener;
import com.example.shopping.app.utility.Utils;
import com.example.shopping.app.adapter.ProductAdapter;
import com.example.shopping.app.model.ProductTemp;
import com.squareup.otto.Subscribe;

import butterknife.Bind;

import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProductListActivity extends ToolbarActivity {
    @Bind(R.id.scroll) RecyclerView mListView;

    @Bind(R.id.load_progress) ProgressBar mLoadProgress;
    @Bind(R.id.load_error_layout) View mLoadErrorView;

    @Bind(R.id.load_more_layout) View mLoadMoreView;
    @Bind(R.id.load_more_progress) ProgressBar mLoadMoreProgress;
    @Bind(R.id.load_more_retry_button) Button mLoadMoreRetryButton;

    private ProductAdapter mListAdapter;
    private GridLayoutManager mLayoutManager;

    private int mLastRequestPage = 1;
    private String mExtraIntentType;
    private String mCategoryName; // FIXME - Category class
    private String mCollectionName; // not used

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // bind views
        bindViews();

        // update toolbar
        updateToolbarTitle(R.string.activity_product_list_title);
        showToolbarBackButton();
        updateToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));

        // preprocess
        mExtraIntentType = getIntent().getStringExtra(Utils.INTENT_TYPE_KEY);
        mCategoryName = getIntent().getStringExtra(Utils.INTENT_CATEGORY_NAME_KEY);

        mLoadProgress.setVisibility(View.VISIBLE);

        // get data from server
        loadDataFromApi();

        mLayoutManager = new GridLayoutManager(this, !Utils.isTablet(this) ? 2 : 3); // FIXME - grid span
        mListView.setLayoutManager(mLayoutManager);
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.setHasFixedSize(true);
        mListAdapter = new ProductAdapter();
        mListView.setAdapter(mListAdapter);
        mListView.addOnScrollListener(new GridEndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // preprocess
                mLoadMoreProgress.setVisibility(View.VISIBLE);
                mLoadMoreView.setVisibility(View.VISIBLE);

                mLastRequestPage = current_page;
                loadDataFromApi(current_page);
            }
        });
    }

    @Subscribe
    public void onProductClicked(ProductTemp product) {
        Intent intent = new Intent(ProductListActivity.this, ProductActivity.class);
        intent.putExtra(Utils.INTENT_PRODUCT_KEY, product.id);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @OnClick(R.id.load_retry_button)
    public void retryLoadDataFromApi() {
        // preprocess
        mLoadErrorView.setVisibility(View.GONE);
        mLoadProgress.setVisibility(View.VISIBLE);

        loadDataFromApi();
    }

    @OnClick(R.id.load_more_retry_button)
    public void retryLoadMoreDataFromApi() {
        // preprocess
        mLoadMoreRetryButton.setVisibility(View.GONE);
        mLoadMoreProgress.setVisibility(View.VISIBLE);

        loadDataFromApi(mLastRequestPage);
    }

    private void loadDataFromApi() {
        this.loadDataFromApi(1);
    }

    private void loadDataFromApi(final int page) {
        if (mExtraIntentType!= null) {
            if (mCategoryName != null && mExtraIntentType.equals(Utils.INTENT__CATEGORY_KEY)) {
                AppController.getInstance().getRestClient().getApiService().getCategoryProductList(
                        mCategoryName, // FIXME - category.slug
                        ProductTemp.fields(),
                        page,
                        new ProductsCallback()
                );
            } else if (mExtraIntentType.equals(Utils.INTENT__COLLECTION_KEY)) {
                AppController.getInstance().getRestClient().getApiService().getProductList(
                        ProductTemp.fields(),
                        page,
                        new ProductsCallback()
                );
            } else if (mExtraIntentType.equals(Utils.INTENT__OFFER_KEY)) {
                AppController.getInstance().getRestClient().getApiService().getPopularProductList(
                        ProductTemp.fields(),
                        page,
                        new ProductsCallback()
                );
            } else if (mExtraIntentType.equals(Utils.INTENT__POPULAR_KEY)) {
                AppController.getInstance().getRestClient().getApiService().getPopularProductList(
                        ProductTemp.fields(),
                        page,
                        new ProductsCallback()
                );
            }
        }
    }

    public class ProductsCallback implements Callback<WC_Products> {
        @Override
        public void success(WC_Products products, Response response) {
            // postprocess
            if (mLastRequestPage > 1) { // more
                mLoadMoreView.setVisibility(View.GONE);
            } else {
                mLoadProgress.setVisibility(View.GONE);
            }

            if (products.products.size() > 0) {
                for (ProductTemp product : products.products) {
                    mListAdapter.add(mListAdapter.getItemCount(), product);
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            // postprocess
            if (mLastRequestPage > 1) { // more
                mLoadMoreProgress.setVisibility(View.GONE);
                mLoadMoreRetryButton.setVisibility(View.VISIBLE);
                mLoadMoreView.setVisibility(View.VISIBLE);
            } else {
                mLoadProgress.setVisibility(View.GONE);
                mLoadErrorView.setVisibility(View.VISIBLE);
            }

            //Toast.makeText(ProductListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}