package com.sleepwalker.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

    /**
     * 把字符串list，用regex分隔符分隔，例如："123","345",末位没有regex
     *
     * @param stringList
     * @param regex
     * @return
     */
    public static String buildStringListToString(List<String> stringList, String regex) {
        String strings = null;
        if (stringList != null && stringList.size() > 0 && StringUtils.isNotBlank(regex)) {
            StringBuilder idBuilder = new StringBuilder();
            for (String id : stringList) {
                if (id != null) {
                    idBuilder.append(regex + id);
                }
            }
            strings = idBuilder.toString();

            if (StringUtils.isNotBlank(strings)) {
                strings = strings.substring(1);
            }
        }
        return strings;
    }

    public static final String randomString(int length) {
        char[] numbersAndLetters = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        if (length < 1) {
            return "";
        }
        Random randGen = new Random();
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(numbersAndLetters.length)];
        }
        return new String(randBuffer);
    }

    /**
     * 把Integer的List，用regex分隔符分隔，例如："123","345",末位没有regex
     *
     * @param idList
     * @param regex
     * @return
     */
    public static String buildIntListToString(List<Integer> idList, String regex) {
        String ids = null;
        if (idList != null && idList.size() > 0 && StringUtils.isNotBlank(regex)) {
            StringBuilder idBuilder = new StringBuilder();
            for (Integer id : idList) {
                if (id != null) {
                    idBuilder.append(regex + id);
                }
            }
            ids = idBuilder.toString();

            if (StringUtils.isNotBlank(ids)) {
                ids = ids.substring(1);
            }
        }
        return ids;
    }

    /**
     * 把int的数组，用regex分隔符分隔，例如："123","345",末位没有regex
     *
     * @param idArr
     * @param regex
     * @return
     */
    public static String buildIntArrToString(int[] idArr, String regex) {
        String ids = null;
        if (idArr != null && idArr.length > 0 && StringUtils.isNotBlank(regex)) {
            StringBuilder idBuilder = new StringBuilder();
            for (int id : idArr) {
                idBuilder.append(regex + id);
            }
            ids = idBuilder.toString();

            if (StringUtils.isNotBlank(ids)) {
                ids = ids.substring(1);
            }
        }
        return ids;
    }

    public static String buildLongListToString(List<Long> list, String regex) {
        String ids = null;
        if (list != null && list.size() > 0 && StringUtils.isNotBlank(regex)) {
            StringBuilder idBuilder = new StringBuilder();
            for (long id : list) {
                idBuilder.append(regex + id);
            }
            ids = idBuilder.toString();

            if (StringUtils.isNotBlank(ids)) {
                ids = ids.substring(1);
            }
        }
        return ids;
    }

    public static String buildLongArrToString(long[] idArr, String regex) {
        String ids = null;
        if (idArr != null && idArr.length > 0 && StringUtils.isNotBlank(regex)) {
            StringBuilder idBuilder = new StringBuilder();
            for (long id : idArr) {
                idBuilder.append(regex + id);
            }
            ids = idBuilder.toString();

            if (StringUtils.isNotBlank(ids)) {
                ids = ids.substring(1);
            }
        }
        return ids;
    }

    /**
     * 把字符串按分隔符分离成Integer的List
     *
     * @param string
     * @param regex
     * @return
     */
    public static List<Integer> splitStringToIntList(String string, String regex) {
        List<Integer> integers = null;

        if (StringUtils.isNotBlank(string) && StringUtils.isNotBlank(regex)) {
            integers = new ArrayList<Integer>();
            String[] stringArr = string.split(regex);
            for (String iter : stringArr) {
                integers.add(Integer.parseInt(iter));
            }
        }
        return integers;
    }

    /**
     * 把字符串按分隔符分离成String的List
     *
     * @param string
     * @param regex
     * @return
     */
    public static List<String> splitStringToStringList(String string, String regex) {
        List<String> strings = null;

        if (StringUtils.isNotBlank(string) && StringUtils.isNotBlank(regex)) {
            strings = new ArrayList<String>();
            String[] stringArr = string.split(regex);
            for (String iter : stringArr) {
                strings.add(iter);
            }
        }
        return strings;
    }
}
