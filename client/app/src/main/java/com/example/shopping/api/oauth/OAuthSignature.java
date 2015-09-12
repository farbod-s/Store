package com.example.shopping.api.oauth;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class OAuthSignature {

    public static String signature(String method, String encodedUrl, String encodedQueryString, String consumerSecret, String algorithm) {
        String signatureBase = createSignatureBase(method, encodedUrl, encodedQueryString);
        return createRequestSignature(signatureBase, consumerSecret, algorithm);
    }

    private static String createSignatureBase(String method, String encodedUrl, String encodedQueryString) {
        StringBuffer sb = new StringBuffer();
        sb.append(method);
        sb.append("&");
        sb.append(encodedUrl);
        sb.append("&");
        sb.append(encodedQueryString);

        return sb.toString();
    }

    private static String createRequestSignature(String signatureBase, String consumerSecret, String algorithm) {
        String signature = null;
        try {
            SecretKeySpec key = new SecretKeySpec(consumerSecret.getBytes("UTF-8"), algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(key);
            signature = Base64.encodeToString(mac.doFinal(signatureBase.getBytes("UTF-8")), Base64.DEFAULT); // NO-WRAP
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return signature; // FIXME - there is a bug, signature produced with empty space! (" " => "+")
    }
}
