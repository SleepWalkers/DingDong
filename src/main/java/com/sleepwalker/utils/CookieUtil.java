package com.sleepwalker.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class CookieUtil {

    public static String getCookieValue(String name, HttpServletRequest request) {
        Cookie cookie = getCookie(name, request);
        if (cookie == null) {
            return null;
        }
        return cookie.getValue();
    }

    public static Cookie getCookie(String name, HttpServletRequest request) {
        if (StringUtils.isBlank(name) || request == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    public static void deleteCookie(Cookie cookie, HttpServletResponse response) {
        if (cookie == null) {
            return;
        }
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void setCookie(String name, String value, int maxAge, HttpServletResponse response) {
        if (StringUtils.isBlank(name) || response == null) {
            return;
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
