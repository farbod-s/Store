package com.example.shopping.api.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;


public class OAuthUtils {
    public static String generateNonce() {
        String nonce = "";
        try {
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = String.valueOf(prng.nextInt());
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result = sha.digest(randomNum.getBytes());
            nonce = OAuthUtils.hexEncode(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nonce;
    }

    public static String generateTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    private static String hexEncode(byte[] aInput) {
        StringBuilder result = new StringBuilder();
        // abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (int i = 0; i < aInput.length; ++i) {
            byte b = aInput[i];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }

        return result.toString();
    }

    public static String URLEncode(String beforeEncode) {
        String afterEncode;
        try {
            beforeEncode = beforeEncode.replace(" ", "&nbsp;");
            afterEncode = URLEncoder.encode(beforeEncode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return afterEncode;
    }

    public static String encode(String input) {
        StringBuilder resultStr = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (isUnSafe(ch)) {
                resultStr.append('%');
                resultStr.append(toHex(ch / 16));
                resultStr.append(toHex(ch % 16));
            } else {
                resultStr.append(ch);
            }
        }
        return resultStr.toString();
    }

    private static char toHex(int ch) {
        return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
    }

    private static boolean isUnSafe(char ch) {
        if (ch > 128 || ch < 0)
            return true;
        return " %*$&+,/:;=?@<>[]{}#%'\n".indexOf(ch) >= 0;
    }
}
