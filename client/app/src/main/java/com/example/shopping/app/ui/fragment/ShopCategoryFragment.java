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
import android.widget.ProgressBar;

import com.example.shopping.R;
import com.example.shopping.api.model.WC_ProductCategories;
import com.example.shopping.app.AppController;
import com.example.shopping.app.utility.Utils;
import com.example.shopping.app.adapter.ShopCategoryAdapter;
import com.example.shopping.app.model.Category;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ShopCategoryFragment extends Fragment {
    @Bind(R.id.scroll) RecyclerView mListView;

    @Bind(R.id.load_progress) ProgressBar mLoadProgress;
    @Bind(R.id.load_error_layout) View mLoadErrorView;

    private ShopCategoryAdapter mListAdapter;
    private GridLayoutManager mLayoutManager;

    // Creates a new fragment
    public static ShopCategoryFragment instance() {
        return new ShopCategoryFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_shop_category, container, false);
        ButterKnife.bind(this, rootView);

        // preprocess
        mLoadProgress.setVisibility(View.VISIBLE);

        // get data from server
        loadDataFromApi();

        mLayoutManager = new GridLayoutManager(getActivity(), !Utils.isTablet(getActivity()) ? 2 : 3); // FIXME - grid span
        mListView.setLayoutManager(mLayoutManager);
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.setHasFixedSize(true);
        mListAdapter = new ShopCategoryAdapter();
        mListView.setAdapter(mListAdapter);

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

    public void loadDataFromApi() {
        AppController.getInstance().getRestClient().getApiService().getCategoryList(
                new Callback<WC_ProductCategories>() {
                    @Override
                    public void success(WC_ProductCategories categories, Response response) {
                        // postprocess
                        mLoadProgress.setVisibility(View.GONE);

                        if (categories.product_categories.size() > 0) {
                            for (Category category : categories.product_categories) {
                                if (category.parent != 0) { // FIXME - remove me!
                                    mListAdapter.add(mListAdapter.getItemCount(), category);
                                }
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // postprocess
                        mLoadProgress.setVisibility(View.GONE);
                        mLoadErrorView.setVisibility(View.VISIBLE);

                        //Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}