package com.example.shopping.app.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.shopping.R;
import com.example.shopping.app.utility.Utils;
import com.example.shopping.app.adapter.FeedAdapter;
import com.example.shopping.app.model.Feed;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class FeedListActivity extends NavigationDrawerActivity {
    @Bind(R.id.scroll) RecyclerView mListView;

    private List<Feed> mFeeds;
    private FeedAdapter mListAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_list);

        // bind views
        bindViews();

        // update toolbar
        updateToolbarTitle(R.string.activity_inbox_title);
        showToolbarDrawerButton();
        setToolbarShoppingCartVisibility(false);
        updateToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));

        // build drawer layout
        buildDrawerLayout(savedInstanceState);

        // update navigation drawer
        updateDrawerItemSelection(DRAWER_ITEM_INBOX_ID);

        this.mFeeds = new ArrayList<>();

        // generate dummy data
        setDummyData();

        mLayoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLayoutManager);
        mListView.setHasFixedSize(true);
        mListAdapter = new FeedAdapter(mFeeds);
        mListView.setAdapter(mListAdapter);
    }

    @Subscribe
    public void onFeedClicked(Feed feed) {
        // TODO
    }

    public void setDummyData() {
        String[] titles = getResources().getStringArray(R.array.items_title_array);
        String[] images = getResources().getStringArray(R.array.items_image_array);
        for (int i = 0; i < 16; i++) {
            mFeeds.add(new Feed(titles[i],
                    images[i],
                    getResources().getString(R.string.dummy_text_description_long),
                    getResources().getString(R.string.dummy_text_datetime),
                    Utils.randInt(0, 23)));
        }
    }
}
