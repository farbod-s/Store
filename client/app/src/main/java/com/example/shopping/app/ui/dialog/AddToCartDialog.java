package com.example.shopping.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.Attribute;
import com.example.shopping.app.model.Item;
import com.example.shopping.app.model.OrderVariation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddToCartDialog extends Dialog {
    @Bind(R.id.cart_attributes_layout) LinearLayout mAttributesLayout;
    @Bind(R.id.quantity_spinner) Spinner mQuantitiesSpinner;

    private ArrayAdapter<String> mQuantitiesAdapter;

    private Map<String, Spinner> mSpinners;
    private List<Attribute> mAttributes;
    private List<OrderVariation> mOptions;
    private List<String> mQuantities;

    private Context mContext;

    public AddToCartDialog(Context context, List<Attribute> attributes) {
        super(context);

        this.mContext = context;
        this.mAttributes = attributes;
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
        setContentView(R.layout.layout_add_to_cart);
        ButterKnife.bind(this);

        // init attributes
        for (Attribute attr : mAttributes) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    attr.options /*options*/);
            Spinner spinner = new Spinner(getContext());
            spinner.setAdapter(adapter);
            mAttributesLayout.addView(spinner);
            mSpinners.put("pa_" + attr.slug, spinner); // {"pa_color":"red"}, ...
        }

        // init quantity
        mQuantitiesAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item,
                mQuantities);
        mQuantitiesSpinner.setAdapter(mQuantitiesAdapter);
    }

    @OnClick(R.id.add_to_cart_button)
    public void onAddToCartClicked() {
        // retrieve options
        mOptions.clear();
        for (Map.Entry<String, Spinner> entry : mSpinners.entrySet()) {
            mOptions.add(new OrderVariation(entry.getKey(),
                    entry.getValue().getSelectedItem().toString()));
        }

        // post item to bus
        Item newItem = new Item();
        newItem.quantity = Integer.valueOf(mQuantitiesSpinner.getSelectedItem().toString());
        newItem.attributes = mAttributes;
        newItem.variations = mOptions;
        AppController.getInstance().getEventBus().post(newItem);

        // auto close!
        this.dismiss();
    }
}
