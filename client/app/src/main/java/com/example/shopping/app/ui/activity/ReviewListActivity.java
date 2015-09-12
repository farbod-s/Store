package com.example.shopping.app.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.shopping.R;
import com.example.shopping.api.model.WC_ProductReviews;
import com.example.shopping.app.AppController;
import com.example.shopping.app.utility.Utils;
import com.example.shopping.app.adapter.ReviewRecyclerAdapter;
import com.example.shopping.app.model.Review;

import butterknife.Bind;
import butterknife.OnClick;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ReviewListActivity extends ToolbarActivity {
    @Bind(R.id.scroll) RecyclerView mListView;

    @Bind(R.id.load_progress) ProgressBar mLoadProgress;
    @Bind(R.id.load_error_layout) View mLoadErrorView;

    @Bind(R.id.load_more_layout) View mLoadMoreView;
    @Bind(R.id.load_more_progress) ProgressBar mLoadMoreProgress;
    @Bind(R.id.load_more_retry_button) Button mLoadMoreRetryButton;

    private ReviewRecyclerAdapter mListAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mProductId;
    private int mLastRequestPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        // bind views
        bindViews();

        // update toolbar
        updateToolbarTitle(R.string.activity_review_title);
        showToolbarBackButton();
        setToolbarShoppingCartVisibility(false);
        setToolbarSearchVisibility(false);
        updateToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));

        // preprocess
        mProductId = getIntent().getIntExtra(Utils.INTENT_PRODUCT_KEY, 0);
        // preprocess
        mLoadProgress.setVisibility(View.VISIBLE);

        // get data from server
        loadDataFromApi();

        mLayoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLayoutManager);
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.setHasFixedSize(true);
        mListAdapter = new ReviewRecyclerAdapter();
        mListView.setAdapter(mListAdapter);
        /*mListView.addOnScrollListener(new LinearEndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // preprocess
                mLoadMoreProgress.setVisibility(View.VISIBLE);
                mLoadMoreView.setVisibility(View.VISIBLE);

                mLastRequestPage = current_page;
                loadDataFromApi(current_page);
            }
        });*/
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
        AppController.getInstance().getRestClient().getApiService().getProductReviewList(
                mProductId,
                new Callback<WC_ProductReviews>() {
                    @Override
                    public void success(WC_ProductReviews productReviews, Response response) {
                        // postprocess
                        if (mLastRequestPage > 1) { // more
                            mLoadMoreView.setVisibility(View.GONE);
                        } else {
                            mLoadProgress.setVisibility(View.GONE);
                        }

                        if (productReviews.product_reviews.size() > 0) {
                            for (Review review : productReviews.product_reviews) {
                                mListAdapter.add(mListAdapter.getItemCount(), review);
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
                    }
                }
        );
    }
}
