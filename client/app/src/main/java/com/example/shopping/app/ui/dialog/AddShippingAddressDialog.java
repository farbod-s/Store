package com.example.shopping.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.shopping.R;
import com.example.shopping.app.model.ShippingAddress;
import com.example.shopping.app.ui.activity.ShippingAddressActivity;
import com.example.shopping.app.utility.XmlCityHandler;
import com.example.shopping.app.utility.XmlStateHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddShippingAddressDialog extends Dialog {
    @Bind(R.id.name_text) EditText mNameText;
    @Bind(R.id.family_text) EditText mFamilyText;
    @Bind(R.id.postalAddress_text) EditText mAddressText;
    @Bind(R.id.postalCode_text) EditText mPostalCodeText;
    @Bind(R.id.emergencyPhone_text) EditText mEmergencyPhone;
    @Bind(R.id.phone_text) EditText mPhoneText;
    @Bind(R.id.city_spinner) Spinner mCitySpinner;
    @Bind(R.id.state_spinner) Spinner mStateSpinner;

    private Context mContext;

    private List<String> mStates;
    private List<String> mCities;

    private ArrayAdapter<String> mStateAdapter;
    private ArrayAdapter<String> mCityAdapter;

    public AddShippingAddressDialog(Context context) {
        super(context);

        this.mContext = context;
        this.mStates = new ArrayList<>();
        this.mCities = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_add_shipping_address);
        ButterKnife.bind(this);

        parseStates();

        mCityAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item,
                mCities);
        mCitySpinner.setAdapter(mCityAdapter);

        mStateAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item,
                mStates);
        mStateSpinner.setAdapter(mStateAdapter);
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCities.clear();
                parseCities(mStateSpinner.getSelectedItem().toString());
                mCityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mCities.clear();
                mCityAdapter.notifyDataSetChanged();
            }
        });
    }

    private void parseStates() {
        AssetManager assetManager = mContext.getAssets();
        try {
            InputStream is = assetManager.open("cities.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            XmlStateHandler handler = new XmlStateHandler();
            xr.setContentHandler(handler);
            InputSource inStream = new InputSource(is);
            xr.parse(inStream);

            mStates.addAll(handler.getStates());

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseCities(String state) {
        AssetManager assetManager = mContext.getAssets();
        try {
            InputStream is = assetManager.open("cities.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            XmlCityHandler handler = new XmlCityHandler(state);
            xr.setContentHandler(handler);
            InputSource inStream = new InputSource(is);
            xr.parse(inStream);

            mCities.addAll(handler.getCities());

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.save_button)
    public void onSaveClicked() {
        final ShippingAddress address = new ShippingAddress();
        address.first_name = mNameText.getText().toString();
        address.last_name = mFamilyText.getText().toString();
        address.state = mStateSpinner.getSelectedItem().toString();
        address.city = mCitySpinner.getSelectedItem().toString();
        address.address_1 = mAddressText.getText().toString();
        address.postcode = mPostalCodeText.getText().toString();

        ((ShippingAddressActivity) mContext).addShippingAddress(address);

        this.dismiss();
    }

    @OnClick(R.id.cancel_button)
    public void onCancelClicked() {
        this.dismiss();
    }
}
