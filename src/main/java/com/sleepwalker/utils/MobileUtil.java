package com.sleepwalker.utils;

import org.apache.commons.lang.StringUtils;

public class MobileUtil {

    private static final String[] chinaMobilePrefix  = { "134", "135", "136", "137", "138", "139",
            "147", "150", "151", "152", "157", "158", "159", "178", "182", "183", "184", "187",
            "188"                                   };

    private static final String[] chinaUnicomPrefix  = { "130", "131", "132", "155", "156", "176",
            "185", "186"                            };

    private static final String[] chinaTelecomPrefix = { "133", "153", "170", "177", "180", "181",
            "189"                                   };

    public static boolean isChinaMobile(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            return false;
        }
        return isContainStr(phoneNumber, chinaMobilePrefix);
    }

    public static boolean isChinaUnicomP(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            return false;
        }
        return isContainStr(phoneNumber, chinaUnicomPrefix);
    }

    public static boolean isTelecomMobile(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            return false;
        }
        return isContainStr(phoneNumber, chinaTelecomPrefix);
    }

    private static boolean isContainStr(String string, String[] prefixArr) {
        for (String prefix : prefixArr) {
            if (string.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
