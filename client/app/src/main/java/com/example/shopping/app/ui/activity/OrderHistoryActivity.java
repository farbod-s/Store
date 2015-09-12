package com.example.shopping.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.shopping.R;
import com.example.shopping.api.model.WC_CustomerOrders;
import com.example.shopping.app.AppController;
import com.example.shopping.app.utility.Utils;
import com.example.shopping.app.adapter.OrderAdapter;
import com.example.shopping.app.model.OrderTemp;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class OrderHistoryActivity extends ToolbarActivity {
    @Bind(R.id.scroll) RecyclerView mListView;

    @Bind(R.id.load_progress) ProgressBar mLoadProgress;
    @Bind(R.id.load_error_layout) View mLoadErrorView;

    @Bind(R.id.load_more_layout) View mLoadMoreView;
    @Bind(R.id.load_more_progress) ProgressBar mLoadMoreProgress;
    @Bind(R.id.load_more_retry_button) Button mLoadMoreRetryButton;

    private OrderAdapter mListAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mCustomerId;
    private int mLastRequestPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // bind views
        bindViews();

        // update toolbar
        updateToolbarTitle(R.string.activity_orders_title);
        showToolbarBackButton();
        setToolbarShoppingCartVisibility(false);
        setToolbarSearchVisibility(false);
        updateToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));

        // preprocess
        mCustomerId = Prefs.getInt(Utils.ACCOUNT_USER_ID_KEY, -1);

        mLoadProgress.setVisibility(View.VISIBLE);

        // get data from server
        loadDataFromApi();

        mLayoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLayoutManager);
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.setHasFixedSize(true);
        mListAdapter = new OrderAdapter();
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
        AppController.getInstance().getRestClient().getApiService().getCustomerOrderList(
                mCustomerId,
                OrderTemp.fields(),
                new Callback<WC_CustomerOrders>() {
                    @Override
                    public void success(WC_CustomerOrders customerOrders, Response response) {
                        // postprocess
                        if (mLastRequestPage > 1) { // more
                            mLoadMoreView.setVisibility(View.GONE);
                        } else {
                            mLoadProgress.setVisibility(View.GONE);
                        }

                        if (customerOrders.orders.size() > 0) {
                            for (OrderTemp order : customerOrders.orders) {
                                mListAdapter.add(mListAdapter.getItemCount(), order);
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

    @Subscribe
    public void onOrderClicked(OrderTemp order) {
        Intent intent = new Intent(OrderHistoryActivity.this, OrderActivity.class);
        intent.putExtra(Utils.INTENT_ORDER_KEY, order.id);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
