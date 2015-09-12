package com.example.shopping.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shopping.R;
import com.example.shopping.api.model.WC_Coupon;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.Coupon;
import com.example.shopping.app.ui.activity.ShoppingCartActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddCouponDialog extends Dialog {
    @Bind(R.id.coupon_load_progress) ProgressBar mProgress;
    @Bind(R.id.coupon_code_text) EditText mCouponText;

    private Context mContext;

    private Coupon mCoupon;

    public AddCouponDialog(Context context) {
        super(context);

        this.mContext = context;
        this.mCoupon = new Coupon();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_add_coupon);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.apply_button)
    public void onApplyClicked() {
        mCouponText.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);

        AppController.getInstance().getRestClient().getApiService().getCoupon(
                mCouponText.getText().toString(),
                new Callback<WC_Coupon>() {
                    @Override
                    public void success(WC_Coupon wc_coupon, Response response) {
                        if (AppController.getInstance().getShoppingCart().applyCoupon(wc_coupon.coupon)) {
                            ((ShoppingCartActivity) mContext).onCouponApplied();
                        }

                        mProgress.setVisibility(View.GONE);
                        mCouponText.setText(R.string.empty);
                        mCouponText.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mProgress.setVisibility(View.GONE);
                        mCouponText.setVisibility(View.VISIBLE);

                        Toast.makeText(mContext, R.string.coupon_error, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    @OnClick(R.id.cancel_button)
    public void onCancelClicked() {
        this.dismiss();
    }
}
