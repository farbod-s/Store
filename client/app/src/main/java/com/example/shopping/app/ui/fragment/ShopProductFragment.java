package com.example.shopping.app.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.shopping.R;
import com.example.shopping.api.model.WC_Products;
import com.example.shopping.app.AppController;
import com.example.shopping.app.ui.widget.GridEndlessScrollListener;
import com.example.shopping.app.utility.Utils;
import com.example.shopping.app.adapter.ProductAdapter;
import com.example.shopping.app.model.ProductTemp;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ShopProductFragment extends Fragment {
    @Bind(R.id.scroll) RecyclerView mListView;

    @Bind(R.id.load_progress) ProgressBar mLoadProgress;
    @Bind(R.id.load_error_layout) View mLoadErrorView;

    @Bind(R.id.load_more_layout) View mLoadMoreView;
    @Bind(R.id.load_more_progress) ProgressBar mLoadMoreProgress;
    @Bind(R.id.load_more_retry_button) Button mLoadMoreRetryButton;

    private ProductAdapter mListAdapter;
    private GridLayoutManager mLayoutManager;

    private int mLastRequestPage = 1;

    // Creates a new fragment
    public static ShopProductFragment instance() {
        return new ShopProductFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_item_list, container, false);
        ButterKnife.bind(this, rootView);

        // preprocess
        mLoadProgress.setVisibility(View.VISIBLE);

        // get data from server
        loadDataFromApi();

        mLayoutManager = new GridLayoutManager(getActivity(), !Utils.isTablet(getActivity()) ? 2 : 3); // FIXME - grid span
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

                loadDataFromApi(current_page);
            }
        });

        return rootView;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        // reset injector
        ButterKnife.unbind(this);
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
        AppController.getInstance().getRestClient().getApiService().getPopularProductList(
                ProductTemp.fields(),
                page,
                new Callback<WC_Products>() {
                    @Override
                    public void success(WC_Products products, Response response) {
                        // postprocess
                        if (page > 1) { // more
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
                        mLastRequestPage = page;
                        if (page > 1) { // more
                            mLoadMoreProgress.setVisibility(View.GONE);
                            mLoadMoreRetryButton.setVisibility(View.VISIBLE);
                            mLoadMoreView.setVisibility(View.VISIBLE);
                        } else {
                            mLoadProgress.setVisibility(View.GONE);
                            mLoadErrorView.setVisibility(View.VISIBLE);
                        }

                        //Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}