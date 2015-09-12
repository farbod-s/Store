package com.example.shopping.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopping.R;
import com.example.shopping.api.model.WC_Authenticate;
import com.example.shopping.api.model.WC_Customer;
import com.example.shopping.app.AppController;
import com.example.shopping.app.utility.Utils;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProfileActivity extends AppCompatActivity {
    @Bind(R.id.customer_username) EditText mUsername;
    @Bind(R.id.customer_password) EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signin);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.signin_button)
    public void signIn() {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        AppController.getInstance().getRestClient().getApiService().authenticate(
                new WC_Authenticate(username, password),
                new Callback<WC_Customer>() {
                    @Override
                    public void success(WC_Customer customer, Response response) {
                        Prefs.putBoolean(Utils.ACCOUNT_LOGGED_IN_KEY, true);
                        Prefs.putString(Utils.ACCOUNT_USERNAME_KEY, customer.customer.username);
                        Prefs.putString(Utils.ACCOUNT_EMAIL_KEY, customer.customer.email);
                        Prefs.putString(Utils.ACCOUNT_FIRST_NAME_KEY, customer.customer.first_name);
                        Prefs.putString(Utils.ACCOUNT_LAST_NAME_KEY, customer.customer.last_name);
                        Prefs.putString(Utils.ACCOUNT_AVATAR_URL_KEY, customer.customer.avatar_url);
                        Prefs.putInt(Utils.ACCOUNT_USER_ID_KEY, customer.customer.id);

                        Intent intent = new Intent(ProfileActivity.this, ShopActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ProfileActivity.this.startActivity(intent);
                        ProfileActivity.this.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(ProfileActivity.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
