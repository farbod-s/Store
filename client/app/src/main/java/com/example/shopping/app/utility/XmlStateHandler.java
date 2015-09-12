package com.example.shopping.app.utility;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class XmlStateHandler extends DefaultHandler {
    private List<String> mStates;

    public XmlStateHandler() {
        mStates = new ArrayList<>();
    }

    public List<String> getStates() {
        return mStates;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("state")) {
            mStates.add(attributes.getValue("name"));
        }
    }
}
