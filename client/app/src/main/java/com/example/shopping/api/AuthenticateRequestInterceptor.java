package com.example.shopping.api;

import com.example.shopping.app.AppController;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class AuthenticateRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .url(AppController
                        .getInstance()
                        .getRestClient()
                        .getSignedUrl(request.url(), request.method()))
                .build();

        return chain.proceed(request);
    }
}
