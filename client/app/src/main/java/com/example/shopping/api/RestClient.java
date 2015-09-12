package com.example.shopping.api;

import com.example.shopping.api.oauth.OAuthConstants;
import com.example.shopping.api.oauth.OAuthRequest;
import com.example.shopping.api.oauth.OAuthSignature;
import com.example.shopping.api.oauth.OAuthUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.net.URL;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;


public class RestClient {
    private static final String END_POINT = "http://fancy.noxod.ir";

    private WoocommerceService mApiService;

    public RestClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .create();

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new AuthenticateRequestInterceptor());

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL) // FIXME - remove later
                .setEndpoint(END_POINT)
                .setClient(new OkClient(client))
                .setConverter(new GsonConverter(gson))
                .build();

        mApiService = restAdapter.create(WoocommerceService.class);
    }

    public WoocommerceService getApiService() {
        return mApiService;
    }

    public String getSignedUrl(URL url, String method) {
        String baseUri = END_POINT + url.getPath();
        OAuthRequest authRequest = new OAuthRequest();
        String signature = OAuthSignature.signature(method.toUpperCase(), // uppercase method
                OAuthUtils.URLEncode(baseUri), // encoded url
                authRequest.getEncodedQueryParams(url.getQuery()), // encoded query params
                OAuthConstants.VALUE__OAUTH_CONSUMER_SECRET,
                OAuthConstants.VALUE__OAUTH_SIGNATURE_METHOD);

        return authRequest.getSignedUrl(baseUri, signature);
    }
}
