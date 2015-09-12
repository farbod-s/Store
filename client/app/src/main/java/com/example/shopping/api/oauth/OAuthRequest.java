package com.example.shopping.api.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


public class OAuthRequest {
    private Map<String, String> mParams;

    public OAuthRequest() {
        this.mParams = new HashMap<>();

        initOAuthParams();
    }

    private void initOAuthParams() {
        mParams.put(OAuthConstants.KEY__OAUTH_CONSUMER_KEY, OAuthConstants.VALUE__OAUTH_CONSUMER_KEY);
        mParams.put(OAuthConstants.KEY__OAUTH_NONCE, OAuthUtils.generateNonce());
        mParams.put(OAuthConstants.KEY__OAUTH_SIGNATURE_METHOD, OAuthConstants.VALUE__OAUTH_SIGNATURE_METHOD);
        mParams.put(OAuthConstants.KEY__OAUTH_TIMESTAMP, OAuthUtils.generateTimeStamp());
        // not needed
        /*mParams.put(OAuthConstants.KEY__OAUTH_VERSION, OAuthConstants.VALUE__OAUTH_VERSION);*/
    }

    private void updateSignature(String signature) {
        mParams.put(OAuthConstants.KEY__OAUTH_SIGNATURE, signature);
    }

    public String getSignedUrl(String url, String signature) {
        updateSignature(signature);
        StringBuffer sb = new StringBuffer();
        sb.append(url);
        sb.append("?");
        sb.append(getQueryParams());

        return sb.toString();
    }

    private String getQueryParams() {
        SortedSet<String> keys = new TreeSet<String>(mParams.keySet()); // sort query params alphabetically
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            String value = mParams.get(key);
            // skip first param
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key + "=" + value);
        }

        return sb.toString();
    }

    public String getQueryParams(String queryParams) {
        addExtraQueryParams(queryParams);

        return getQueryParams();
    }

    private String getEncodedQueryParams() {
        SortedSet<String> keys = new TreeSet<String>(mParams.keySet()); // sort query params alphabetically
        StringBuilder sb = new StringBuilder();
        boolean unicodeKey;
        boolean unicodeValue;
        for (String key : keys) {
            String value = mParams.get(key);
            // check unicode value
            unicodeKey = (key.contains("%"));
            unicodeValue = (value.contains("%"));
            // encode params
            String encodedKey = OAuthUtils.URLEncode(key);
            String encodedValue = OAuthUtils.URLEncode(value);
            // percent symbols (%) must be double-encoded
            if (encodedKey != null && encodedKey.contains("%") && !unicodeKey) {
                encodedKey = encodedKey.replace("%", "%25");
            }
            if (encodedValue != null && encodedValue.contains("%") && !unicodeValue) {
                encodedValue = encodedValue.replace("%", "%25");
            }
            if (unicodeKey) {
                // FIXME - ugly fix for unicode characters
                encodedKey = encodedKey.toUpperCase(); // FIXME - may produce NULL Exception
            }
            if (unicodeValue) {
                // FIXME - ugly fix for unicode characters
                encodedValue = encodedValue.toUpperCase(); // FIXME - may produce NULL Exception
            }
            // skip first param
            if (sb.length() > 0) {
                sb.append("%26"); // => '&'
            }
            sb.append(encodedKey);
            sb.append("%3D"); // => '='
            sb.append(encodedValue);
        }

        return sb.toString();
    }

    public String getEncodedQueryParams(String queryParams) {
        addExtraQueryParams(queryParams);

        return getEncodedQueryParams();
    }

    private void addExtraQueryParams(String queryParams) {
        // add extra query params
        if (queryParams != null) {
            String[] pairs = queryParams.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                try {
                    mParams.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                            URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
