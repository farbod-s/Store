package com.example.shopping.app.utility;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Random;


public class Utils {
    public final static String ACCOUNT_LOGGED_IN_KEY = "key_account_logged_in";
    public final static String ACCOUNT_USERNAME_KEY = "key_account_username";
    public final static String ACCOUNT_EMAIL_KEY = "key_account_email";
    public final static String ACCOUNT_FIRST_NAME_KEY = "key_account_first_name";
    public final static String ACCOUNT_LAST_NAME_KEY = "key_account_last_name";
    public final static String ACCOUNT_AVATAR_URL_KEY = "key_account_avatar_url";
    public final static String ACCOUNT_USER_ID_KEY = "key_account_user_id";

    public final static String INTENT_TYPE_KEY = "key_intent_type";
    public final static String INTENT_CATEGORY_NAME_KEY = "key_category_name";
    public final static String INTENT_PRODUCT_KEY = "key_product";
    public final static String INTENT_ORDER_KEY = "key_order";

    public final static String INTENT__COLLECTION_KEY = "key_intent_collection";
    public final static String INTENT__CATEGORY_KEY = "key_intent_category";
    public final static String INTENT__POPULAR_KEY = "key_intent_popular"; // most sales
    public final static String INTENT__OFFER_KEY = "key_intent_offer"; // special offer

    private static Utils instance;
    private Typeface typeface;

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    private Utils() {
        // nothing
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int getDisplayWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    public static String getStringValue(Integer input) {
        String return_value;
        if (input < 0)
            return_value = "-";
        else if (input >= 0 && input <= 50)
            return_value = String.valueOf(input);
        else if (input > 50 && input < 100)
            return_value = "50+";
        else
            return_value = "99+";

        return return_value;
    }

    public static int randInt(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public void prepareTextView(TextView textView) {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/iran.ttf");
        }
        textView.setTypeface(typeface);
    }

    public void prepareTextViewBold(TextView textView) {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/iran.ttf");
        }
        textView.setTypeface(typeface, Typeface.BOLD);
    }
}
