package com.sleepwalker.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/**
 * UrlBase64加解密算法
 */
public class UrlBase64CoderUtil {
    public final static String ENCODING = "UTF-8";

    // 加密  
    public static String encoded(String data) throws UnsupportedEncodingException {
        byte[] b = Base64.encodeBase64URLSafe(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    // 解密  
    public static String decode(String data) throws UnsupportedEncodingException {
        byte[] b = Base64.decodeBase64(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    public static void main(String[] arg) throws UnsupportedEncodingException {
        String encoded = encoded("HZCRrTA/RDM=");
        System.out.println(encoded);
        String decode = decode(encoded);
        System.out.println(decode);
    }
}
