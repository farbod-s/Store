package com.example.shopping.app;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.example.shopping.R;
import com.example.shopping.api.RestClient;
import com.example.shopping.app.model.ShoppingCart;
import com.example.shopping.materialdrawer.util.DrawerImageLoader;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class AppController extends Application {
    private static AppController mInstance;
    private static Bus mEventBus;
    private static RestClient mRestClient;
    private static ShoppingCart mShoppingCart;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        // init font faces
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/IRAN-Sans.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        // Initialize the Prefs class
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        // initialize and create the image loader logic
        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public Bus getEventBus() {
        if (mEventBus == null) {
            // want to be able to send events from any thread (?)
            mEventBus = new Bus(ThreadEnforcer.MAIN);
        }
        return mEventBus;
    }

    public RestClient getRestClient() {
        if (mRestClient == null) {
            mRestClient = new RestClient();
        }
        return mRestClient;
    }

    public ShoppingCart getShoppingCart() {
        if (mShoppingCart == null) {
            mShoppingCart = new ShoppingCart();
        }
        return mShoppingCart;
    }
}
