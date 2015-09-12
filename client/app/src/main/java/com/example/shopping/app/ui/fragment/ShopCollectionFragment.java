package com.example.shopping.app.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopping.R;
import com.example.shopping.app.adapter.ShopCollectionAdapter;
import com.example.shopping.app.model.Collection;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;


public class ShopCollectionFragment extends Fragment {
    @Bind(R.id.scroll) RecyclerView mListView;

    private List<Collection> mCollections;
    private ShopCollectionAdapter mListAdapter;
    private LinearLayoutManager mLayoutManager;

    // Creates a new fragment
    public static ShopCollectionFragment instance() {
        return new ShopCollectionFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCollections = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_item_list, container, false);
        ButterKnife.bind(this, rootView);

        // generate dummy data
        setDummyData();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(mLayoutManager);
        mListView.setHasFixedSize(true);
        mListAdapter = new ShopCollectionAdapter(mCollections);
        mListView.setAdapter(mListAdapter);

        return rootView;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        // reset injector
        ButterKnife.unbind(this);
    }

    public void setDummyData() {
        String[] titles = getActivity().getResources().getStringArray(R.array.collections_title_array);
        String[] images = getActivity().getResources().getStringArray(R.array.collections_image_array);
        for (int i = 0; i < 4; i++) {
            mCollections.add(new Collection(titles[i], images[(i*5)], images[(i*5) + 1], images[(i*5) + 2], images[(i*5) + 3], images[(i*5) + 4]));
        }
    }
}