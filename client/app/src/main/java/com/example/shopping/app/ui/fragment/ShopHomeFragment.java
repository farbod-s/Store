package com.example.shopping.app.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.utility.Utils;
import com.example.shopping.app.adapter.ProductHorizontalAdapter;
import com.example.shopping.app.model.Collection;
import com.example.shopping.app.model.ProductTemp;
import com.example.shopping.app.ui.activity.ProductListActivity;
import com.example.shopping.app.ui.activity.ShopActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;


public class ShopHomeFragment extends Fragment {
    @Bind(R.id.special_offer_list) RecyclerView mSpecialOfferList;
    @Bind(R.id.most_sales_list) RecyclerView mMostSalesList;

    @Bind(R.id.item_collection_image) ImageView mRecentCollectionImage;
    @Bind(R.id.item_collection_image_more_1) ImageView mRecentCollectionImageMore1;
    @Bind(R.id.item_collection_image_more_2) ImageView mRecentCollectionImageMore2;
    @Bind(R.id.item_collection_image_more_3) ImageView mRecentCollectionImageMore3;
    @Bind(R.id.item_collection_image_more_4) ImageView mRecentCollectionImageMore4;
    @Bind(R.id.item_collection_title) TextView mRecentCollectionTitle;

    @Bind(R.id.first_banner_layout) View firstBannerView;
    @Bind(R.id.second_banner_layout) View secondBannerView;

    @Bind(R.id.scroll) ScrollView mScrollView;

    private Collection mRecentCollection;

    private List<ProductTemp> mSpecialOfferProducts;
    private ProductHorizontalAdapter mSpecialOfferAdapter;
    private LinearLayoutManager mSpecialOfferLayoutManager;

    private List<ProductTemp> mMostSalesProducts;
    private ProductHorizontalAdapter mMostSalesAdapter;
    private LinearLayoutManager mMostSalesLayoutManager;

    private Context mContext;

    // Creates a new fragment
    public static ShopHomeFragment instance() {
        return new ShopHomeFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSpecialOfferProducts = new ArrayList<>();
        this.mMostSalesProducts = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_home, container, false);
        ButterKnife.bind(this, rootView);

        ImageView firstBannerImage = (ImageView) firstBannerView.findViewById(R.id.banner_image);
        ImageView firstBannerBadge = (ImageView) firstBannerView.findViewById(R.id.banner_badge);
        TextView firstBannerText = (TextView) firstBannerView.findViewById(R.id.banner_title);
        View firstBannerMore = firstBannerView.findViewById(R.id.banner_more_layout);
        Picasso.with(mContext).load("http://2.187.253.46/Digikala/Image/Public/vtwo/perfume-all.png").into(firstBannerImage);
        firstBannerBadge.setVisibility(View.VISIBLE);
        firstBannerText.setText(mContext.getResources().getString(R.string.banner_title));

        ImageView secondBannerImage = (ImageView) secondBannerView.findViewById(R.id.banner_image);
        TextView secondBannerText = (TextView) secondBannerView.findViewById(R.id.banner_title);
        View secondBannerMore = secondBannerView.findViewById(R.id.banner_more_layout);
        Picasso.with(mContext).load("http://www.shixon.com/Images/MenuImg/accessories.jpg").into(secondBannerImage);
        secondBannerText.setText(mContext.getResources().getString(R.string.banner_title_2));

        // generate special offer dummy data
        setSpecialOfferDummyData();

        mSpecialOfferLayoutManager = new LinearLayoutManager(mContext);
        mSpecialOfferLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSpecialOfferList.setLayoutManager(mSpecialOfferLayoutManager);
        mSpecialOfferList.setHasFixedSize(true);
        mSpecialOfferAdapter = new ProductHorizontalAdapter();
        mSpecialOfferList.setAdapter(mSpecialOfferAdapter);

        // generate most sales dummy data
        setMostSalesDummyData();

        mMostSalesLayoutManager = new LinearLayoutManager(mContext);
        mMostSalesLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMostSalesList.setLayoutManager(mMostSalesLayoutManager);
        mMostSalesList.setHasFixedSize(true);
        mMostSalesAdapter = new ProductHorizontalAdapter();
        mMostSalesList.setAdapter(mMostSalesAdapter);

        // generate recent collection dummy data
        setRecentCollectionDummyData();

        Picasso.with(mContext).load(mRecentCollection.imageCover).into(mRecentCollectionImage);
        Picasso.with(mContext).load(mRecentCollection.imageMore1).into(mRecentCollectionImageMore1);
        Picasso.with(mContext).load(mRecentCollection.imageMore2).into(mRecentCollectionImageMore2);
        Picasso.with(mContext).load(mRecentCollection.imageMore3).into(mRecentCollectionImageMore3);
        Picasso.with(mContext).load(mRecentCollection.imageMore4).into(mRecentCollectionImageMore4);
        mRecentCollectionTitle.setText(mRecentCollection.title);

        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // reset injector
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.special_offer_more_button)
    public void specialOfferMoreClickHandler() {
        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        intent.putExtra(Utils.INTENT_TYPE_KEY, Utils.INTENT__OFFER_KEY);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @OnClick(R.id.most_sales_more_button)
    public void mostSalesMoreClickHandler() {
        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        intent.putExtra(Utils.INTENT_TYPE_KEY, Utils.INTENT__POPULAR_KEY);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @OnClick(R.id.recent_collection_more_button)
    public void recentCollectionMoreClickHandler() {
        ((ShopActivity) getActivity()).getViewPager().setCurrentItem(0);
    }

    @OnClick(R.id.recent_collection_item)
    public void recentCollectionClickHandler() {
        AppController.getInstance().getEventBus().post(mRecentCollection);
    }

    private void setSpecialOfferDummyData() {
        String[] titles = mContext.getResources().getStringArray(R.array.products_title_array);
        String[] images = mContext.getResources().getStringArray(R.array.products_image_array);
        for (int i = 5; i < 10; i++) {
            mSpecialOfferProducts.add(new ProductTemp(i,
                    titles[i],
                    78000.0f,
                    105000.0f,
                    78000.0f,
                    String.valueOf(78000) + " " + mContext.getResources().getString(R.string.currency),
                    true,
                    images[i]));
        }
    }

    private void setMostSalesDummyData() {
        String[] titles = mContext.getResources().getStringArray(R.array.products_title_array);
        String[] images = mContext.getResources().getStringArray(R.array.products_image_array);
        for (int i = 10; i < 15; i++) {
            mMostSalesProducts.add(new ProductTemp(i,
                    titles[i],
                    78000.0f,
                    105000.0f,
                    78000.0f,
                    String.valueOf(78000) + " " + mContext.getResources().getString(R.string.currency),
                    true,
                    images[i]));
        }
    }

    private void setRecentCollectionDummyData() {
        String[] titles = mContext.getResources().getStringArray(R.array.collections_title_array);
        String[] images = mContext.getResources().getStringArray(R.array.collections_image_array);
        int i = 0;
        this.mRecentCollection = new Collection(titles[i], images[(i*5)], images[(i*5) + 1], images[(i*5) + 2], images[(i*5) + 3], images[(i*5) + 4]);
    }
}
