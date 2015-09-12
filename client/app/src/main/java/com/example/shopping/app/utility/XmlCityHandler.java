package com.example.shopping.app.utility;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class XmlCityHandler extends DefaultHandler {
    private boolean mCurrentState = false;
    private boolean mCurrentElement = false;
    private String mCurrentValue = "";

    private String mState;
    private List<String> mCities;

    public XmlCityHandler(String state) {
        mCities = new ArrayList<>();
        mState = state;
    }

    public List<String> getCities() {
        return mCities;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("state") && attributes.getValue("name").equals(mState)) {
            mCurrentState = true;
        } else if (qName.equalsIgnoreCase("city") && mCurrentState) {
            mCurrentElement = true;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        mCurrentElement = false;
        if (qName.equalsIgnoreCase("state")) {
            mCurrentState = false;
        } else if (qName.equalsIgnoreCase("city") && mCurrentState) {
            mCities.add(mCurrentValue);
        }
        mCurrentValue = "";
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (mCurrentElement) {
            mCurrentValue = mCurrentValue + new String(ch, start, length);
        }
    }
}
