package com.example.shopping.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.Item;
import com.example.shopping.app.ui.activity.ShoppingCartActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class RemoveFromCartDialog extends Dialog {
    private Context mContext;

    private Item mItem;

    public RemoveFromCartDialog(Context context, Item item) {
        super(context);

        this.mContext = context;
        this.mItem = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_remove_from_cart);
        ButterKnife.bind(this);
    }

    public void update(Item item) {
        this.mItem = item;
    }

    @OnClick(R.id.ok_button)
    public void onOkClicked() {
        ((ShoppingCartActivity)mContext).onItemRemoved(mItem);

        // auto close!
        this.dismiss();
    }

    @OnClick(R.id.cancel_button)
    public void onCancelClicked() {
        this.dismiss();
    }
}
