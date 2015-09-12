package com.example.shopping.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.Attribute;
import com.example.shopping.app.model.Item;
import com.example.shopping.app.model.OrderVariation;
import com.example.shopping.app.ui.activity.ShoppingCartActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditInCartDialog extends Dialog {
    private final static String tag = EditInCartDialog.class.getSimpleName();

    @Bind(R.id.cart_attributes_layout) LinearLayout mAttributesLayout;
    @Bind(R.id.quantity_spinner) Spinner mQuantitiesSpinner;

    private ArrayAdapter<String> mQuantitiesAdapter;

    private Map<String, Spinner> mSpinners;
    private List<OrderVariation> mOptions;
    private List<String> mQuantities;

    private Context mContext;

    private Item mItem;

    public EditInCartDialog(Context context, Item item) {
        super(context);

        this.mContext = context;
        this.mItem = item;

        this.mSpinners = new HashMap<>();
        this.mOptions = new ArrayList<>();

        this.mQuantities = new ArrayList<>();
        for (int i = 1; i < 11; ++i) { // 1:10
            mQuantities.add(String.valueOf(i));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_edit_in_cart);
        ButterKnife.bind(this);

        // update dialog data
        update();
    }

    public void update(Item item) {
        this.mItem = item;

        this.mSpinners.clear();
        this.mOptions.clear();

        mAttributesLayout.removeAllViews();

        // update dialog data
        update();
    }

    private void update() {
        // init attributes
        for (Attribute attr : mItem.attributes) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    attr.options /*options*/);
            Spinner spinner = new Spinner(getContext());
            spinner.setAdapter(adapter);
            mAttributesLayout.addView(spinner);
            final String key = "pa_" + attr.slug;
            mSpinners.put(key, spinner); // {"pa_color":"red"}, ...

            // update selected index
            searchOptions: {
                for (OrderVariation var : mItem.variations) {
                    if (var.key.equals(key)) {
                        int index = 0;
                        for (String opt : attr.options) {
                            if (opt.equals(var.value)) {
                                spinner.setSelection(index);
                                break searchOptions;
                            }
                            index++;
                        }
                    }
                }
            }
        }

        // init quantity
        mQuantitiesAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item,
                mQuantities);
        mQuantitiesSpinner.setAdapter(mQuantitiesAdapter);

        // update selected index
        mQuantitiesSpinner.setSelection(mItem.quantity - 1);
    }

    @OnClick(R.id.save_button)
    public void onSaveClicked() {
        // retrieve options
        for (Map.Entry<String, Spinner> entry : mSpinners.entrySet()) {
            mOptions.add(new OrderVariation(entry.getKey(),
                    entry.getValue().getSelectedItem().toString()));
        }

        // post item to bus
        mItem.quantity = Integer.valueOf(mQuantitiesSpinner.getSelectedItem().toString());
        mItem.variations = mOptions;

        ((ShoppingCartActivity)mContext).onItemEdited(mItem);

        // auto close!
        this.dismiss();
    }

    @OnClick(R.id.cancel_button)
    public void onCancelClicked() {
        this.dismiss();
    }
}
