package com.example.shopping.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.shopping.R;
import com.example.shopping.app.ui.fragment.ShopCategoryFragment;
import com.example.shopping.app.ui.fragment.ShopCollectionFragment;
import com.example.shopping.app.ui.fragment.ShopHomeFragment;
import com.example.shopping.app.ui.fragment.ShopProductFragment;


public class ShopTabsPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES;

    private ShopHomeFragment mHomeFragment;
    private ShopCategoryFragment mCategoriesFragment;
    private ShopProductFragment mPopularsFragment;
    private ShopCollectionFragment mCollectionsFragment;

    public ShopTabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        TITLES = context.getResources().getStringArray(R.array.shop_tabs_title_list);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 3:
                // HOME
                if (mHomeFragment == null)
                    mHomeFragment = ShopHomeFragment.instance();

                return mHomeFragment;
            case 2:
                // CATEGORIES
                if (mCategoriesFragment == null)
                    mCategoriesFragment = ShopCategoryFragment.instance();

                return mCategoriesFragment;
            case 1:
                // POPULARS
                if (mPopularsFragment == null)
                    mPopularsFragment = ShopProductFragment.instance();

                return mPopularsFragment;
            case 0:
                // COLLECTIONS
                if (mCollectionsFragment == null)
                    mCollectionsFragment = ShopCollectionFragment.instance();

                return mCollectionsFragment;
        }

        return null;
    }
}
