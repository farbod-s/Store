package com.example.shopping.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.example.shopping.app.AppController;
import com.example.shopping.app.utility.Utils;
import com.example.shopping.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import com.example.shopping.R;
import com.example.shopping.materialdrawer.Drawer;
import com.example.shopping.materialdrawer.DrawerBuilder;
import com.example.shopping.materialdrawer.accountswitcher.AccountHeader;
import com.example.shopping.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.example.shopping.materialdrawer.model.DividerDrawerItem;
import com.example.shopping.materialdrawer.model.PrimaryDrawerItem;
import com.example.shopping.materialdrawer.model.ProfileDrawerItem;
import com.example.shopping.materialdrawer.model.interfaces.IDrawerItem;
import com.example.shopping.materialdrawer.model.interfaces.IProfile;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.OnClick;


public class NavigationDrawerActivity extends ToolbarActivity {
    public final static int DRAWER_ITEM_SHOP_ID = 1;
    public final static int DRAWER_ITEM_INBOX_ID = 2;
    public final static int DRAWER_ITEM_SHOPPING_CART_ID = 3;
    public final static int DRAWER_ITEM_GIFT_CARD_ID = 4;
    public final static int DRAWER_ITEM_PICKS_ID = 5;
    public final static int DRAWER_ITEM_NOTIFICATIONS_ID = 6;
    public final static int DRAWER_ITEM_INVITE_FRIENDS_ID = 7;
    public final static int DRAWER_ITEM_FIND_FRIENDS_ID = 8;
    public final static int DRAWER_ITEM_SETTINGS_ID = 9;
    public final static int DRAWER_ITEM_ONLINE_SUPPORT_ID = 10;
    public final static int DRAWER_ITEM_ABOUT_US_ID = 11;
    //public final static int DRAWER_ITEM_ACCOUNT_SETTINGS_ID = 12;

    private final static int DRAWER_ITEMS_COUNT = 12;

    // save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;

    private final Handler mDrawerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // update shopping cart badge
        int count = AppController.getInstance().getShoppingCart().getItemsCount();
        if (count > 0) {
            updateDrawerItemBadge(DRAWER_ITEM_SHOPPING_CART_ID, count);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick(R.id.btn_drawer)
    public void toolbarDrawerHandler(View view) {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else if (result != null && !result.isDrawerOpen()) {
            result.openDrawer();
        }
    }

    private void createGuestAccountLayout(Bundle savedInstanceState) {
        // Create Profile
        final IProfile profile = new ProfileDrawerItem()
                .withName(getString(R.string.profile_name))
                .withEmail(getString(R.string.profile_email));
                //.withIcon(getString(R.string.profile_icon));

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                        //.withCompactStyle(true)
                .withHeaderBackground(R.color.primary)
                .addProfiles(
                        profile
                        //new ProfileSettingDrawerItem().withName(getString(R.string.drawer_item_account_settings)).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(DRAWER_ITEM_ACCOUNT_SETTINGS_ID)
                )
                .withCurrentProfileHiddenInList(true)
                .withSelectionFistLineShown(true) // user name
                .withSelectionSecondLineShown(true) // user email
                .withSelectionSecondLine(getString(R.string.login_profile_text))
                .withSelectionListEnabled(false) // hide profiles setting list
                .withSelectionListEnabledForSingleProfile(false) // hide profiles setting list
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        mDrawerHandler.removeCallbacksAndMessages(null);
                        mDrawerHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(NavigationDrawerActivity.this, ProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                NavigationDrawerActivity.this.startActivity(intent);
                                NavigationDrawerActivity.this.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                            }
                        }, 450);

                        if (result != null && result.isDrawerOpen()) {
                            result.closeDrawer();
                        }
                        return false;
                    }
                })
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        mDrawerHandler.removeCallbacksAndMessages(null);
                        mDrawerHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(NavigationDrawerActivity.this, ProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                NavigationDrawerActivity.this.startActivity(intent);
                                NavigationDrawerActivity.this.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                            }
                        }, 450);

                        // false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        // only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the active profile
            headerResult.setActiveProfile(profile);
        }
    }

    private void createAccountLayout(Bundle savedInstanceState) {
        // Create Profile
        final IProfile profile = new ProfileDrawerItem()
                .withName(Prefs.getString(Utils.ACCOUNT_FIRST_NAME_KEY, "") + " " + Prefs.getString(Utils.ACCOUNT_LAST_NAME_KEY, ""))
                .withEmail(Prefs.getString(Utils.ACCOUNT_EMAIL_KEY, ""))
                .withIcon(Prefs.getString(Utils.ACCOUNT_AVATAR_URL_KEY, ""));

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                //.withCompactStyle(true)
                .withHeaderBackground(R.color.primary)
                .addProfiles(
                        profile
                        //new ProfileSettingDrawerItem().withName(getString(R.string.drawer_item_account_settings)).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(DRAWER_ITEM_ACCOUNT_SETTINGS_ID)
                )
                .withCurrentProfileHiddenInList(true)
                .withSelectionFistLineShown(true) // user name
                .withSelectionSecondLineShown(true) // user email
                .withSelectionSecondLine(getString(R.string.view_profile_text))
                .withSelectionListEnabled(false) // hide profiles setting list
                .withSelectionListEnabledForSingleProfile(false) // hide profiles setting list
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        mDrawerHandler.removeCallbacksAndMessages(null);
                        mDrawerHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(NavigationDrawerActivity.this, ProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                NavigationDrawerActivity.this.startActivity(intent);
                                NavigationDrawerActivity.this.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                            }
                        }, 450);

                        if (result != null && result.isDrawerOpen()) {
                            result.closeDrawer();
                        }
                        return false;
                    }
                })
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        mDrawerHandler.removeCallbacksAndMessages(null);
                        mDrawerHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(NavigationDrawerActivity.this, ProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                NavigationDrawerActivity.this.startActivity(intent);
                                NavigationDrawerActivity.this.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                            }
                        }, 450);

                        // false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        // only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the active profile
            headerResult.setActiveProfile(profile);
        }
    }

    public void buildDrawerLayout(Bundle savedInstanceState) {
        if (Prefs.getBoolean(Utils.ACCOUNT_LOGGED_IN_KEY, false)) { // already logged in
            createAccountLayout(savedInstanceState);
            createDrawerLayout(savedInstanceState);
        } else { // guest user
            createGuestAccountLayout(savedInstanceState);
            createGuestDrawerLayout(savedInstanceState);
        }
    }

    private void createGuestDrawerLayout(Bundle savedInstanceState) {
        // Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withDrawerGravity(Gravity.RIGHT)
                .withAccountHeader(headerResult) // set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_shop).withIcon(GoogleMaterial.Icon.gmd_store).withIdentifier(DRAWER_ITEM_SHOP_ID).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_inbox).withIcon(GoogleMaterial.Icon.gmd_trending_up).withIdentifier(DRAWER_ITEM_INBOX_ID).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_shopping_cart).withIcon(GoogleMaterial.Icon.gmd_shopping_cart).withIdentifier(DRAWER_ITEM_SHOPPING_CART_ID).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_gift_cards).withIcon(GoogleMaterial.Icon.gmd_card_giftcard).withIdentifier(DRAWER_ITEM_GIFT_CARD_ID).withCheckable(false),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_online_support).withIdentifier(DRAWER_ITEM_ONLINE_SUPPORT_ID).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_about_us).withIdentifier(DRAWER_ITEM_ABOUT_US_ID).withCheckable(false)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (result.getCurrentSelection() == position) {
                            return false; // do nothing + close drawer
                        }

                        final IDrawerItem dItem = drawerItem;
                        if (dItem != null) {
                            mDrawerHandler.removeCallbacksAndMessages(null);
                            mDrawerHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = null;
                                    if (dItem.getIdentifier() == DRAWER_ITEM_SHOP_ID) {
                                        intent = new Intent(NavigationDrawerActivity.this, ShopActivity.class);
                                    } else if (dItem.getIdentifier() == DRAWER_ITEM_INBOX_ID) {
                                        intent = new Intent(NavigationDrawerActivity.this, FeedListActivity.class);
                                    } else if (dItem.getIdentifier() == DRAWER_ITEM_SHOPPING_CART_ID) {
                                        intent = new Intent(NavigationDrawerActivity.this, ShoppingCartActivity.class);
                                    }
                                    if (intent != null) {
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        NavigationDrawerActivity.this.startActivity(intent);
                                        NavigationDrawerActivity.this.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                                    }
                                }
                            }, 450);
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                        //.withShowDrawerOnFirstLaunch(true)
                .build();
    }

    private void createDrawerLayout(Bundle savedInstanceState) {
        // Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withDrawerGravity(Gravity.RIGHT)
                .withAccountHeader(headerResult) // set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_shop).withIcon(GoogleMaterial.Icon.gmd_store).withIdentifier(DRAWER_ITEM_SHOP_ID).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_inbox).withIcon(GoogleMaterial.Icon.gmd_trending_up).withIdentifier(DRAWER_ITEM_INBOX_ID).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_shopping_cart).withIcon(GoogleMaterial.Icon.gmd_shopping_cart).withIdentifier(DRAWER_ITEM_SHOPPING_CART_ID).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_gift_cards).withIcon(GoogleMaterial.Icon.gmd_card_giftcard).withIdentifier(DRAWER_ITEM_GIFT_CARD_ID).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_picks).withIcon(GoogleMaterial.Icon.gmd_favorite).withIdentifier(DRAWER_ITEM_PICKS_ID).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_notification).withIcon(GoogleMaterial.Icon.gmd_notifications).withIdentifier(DRAWER_ITEM_NOTIFICATIONS_ID).withCheckable(false),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIdentifier(DRAWER_ITEM_SETTINGS_ID).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_invite_friends).withIdentifier(DRAWER_ITEM_INVITE_FRIENDS_ID).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_online_support).withIdentifier(DRAWER_ITEM_ONLINE_SUPPORT_ID).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_about_us).withIdentifier(DRAWER_ITEM_ABOUT_US_ID).withCheckable(false)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (result.getCurrentSelection() == position) {
                            return false; // do nothing + close drawer
                        }

                        final IDrawerItem dItem = drawerItem;
                        if (dItem != null) {
                            mDrawerHandler.removeCallbacksAndMessages(null);
                            mDrawerHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = null;
                                    if (dItem.getIdentifier() == DRAWER_ITEM_SHOP_ID) {
                                        intent = new Intent(NavigationDrawerActivity.this, ShopActivity.class);
                                    } else if (dItem.getIdentifier() == DRAWER_ITEM_INBOX_ID) {
                                        intent = new Intent(NavigationDrawerActivity.this, FeedListActivity.class);
                                    } else if (dItem.getIdentifier() == DRAWER_ITEM_SHOPPING_CART_ID) {
                                        intent = new Intent(NavigationDrawerActivity.this, ShoppingCartActivity.class);
                                    }
                                    if (intent != null) {
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        NavigationDrawerActivity.this.startActivity(intent);
                                        NavigationDrawerActivity.this.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                                    }
                                }
                            }, 450);
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                        //.withShowDrawerOnFirstLaunch(true)
                .build();
    }

    public boolean updateDrawerItemSelection(int id) {
        if (id >= 1 && id <= DRAWER_ITEMS_COUNT) {
            result.setSelectionByIdentifier(id, false);
            return true;
        }
        return false;
    }

    public boolean updateDrawerItemBadge(int id, int count) {
        if (id >= 1 && id <= DRAWER_ITEMS_COUNT) {
            result.updateBadge(Utils.getStringValue(count), result.getPositionFromIdentifier(id));
            return true;
        }
        return false;
    }
}