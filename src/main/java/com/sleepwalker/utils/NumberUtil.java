package com.sleepwalker.utils;

import java.util.Random;

public class NumberUtil {

    /**
     * 获取小数的小数点前整数
     *
     * @param rate
     * @return
     */
    public static int getRateDigit(double rate) {
        String rateString = Double.toString(Math.floor(rate * 10) / 10.0);
        int dotIndex = rateString.indexOf('.');
        return Integer.parseInt(rateString.substring(0, dotIndex));
    }

    public static int rangeRandomWithRange(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    /**
     * 生成范围内随机长整型数，包括边界
     * @param min
     * @param max
     * @return
     */
    public static long rangeRandomWithRange(long min, long max) {
        Random r = new Random();
        long number = min + ((long) (r.nextDouble() * (max - min)));

        return number;
    }

    /**
     * 获得小数的小数点后的整数
     *
     * @param rate
     * @return
     */
    public static int getRateDecimal(double rate) {
        String rateString = Double.toString(Math.floor(rate * 10) / 10.0);

        int dotIndex = rateString.indexOf('.');

        return Integer.parseInt(rateString.substring(dotIndex + 1, rateString.length()));
    }

    /**
     * 生成范围内随机整数，不包括边界
     * @param min
     * @param max
     * @return
     */
    public static int rangeRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        while (s == min || s == max) {
            rangeRandom(min, max);
        }
        return s;
    }

    /**
     * 生成范围内随机长整型数，不包括边界
     * @param min
     * @param max
     * @return
     */
    public static long rangeRandom(long min, long max) {
        Random r = new Random();
        long number = min + ((long) (r.nextDouble() * (max - min)));
        while (number == min || number == max) {
            rangeRandom(min, max);
        }
        return number;
    }

    /**
     * 获取指定长度的随机非负整数
     *
     * @param length
     * @return
     */
    public static int getRandomIntByLength(int length) {
        StringBuilder result = new StringBuilder();
        Random rnd = new Random();
        int first = rnd.nextInt(9) + 1;//在1-9之间随机生成一个数，保证第一位不为0
        result.append(first);

        for (int i = 1; i <= length - 1; i++) {
            int other = rnd.nextInt(10);
            result.append(other);
        }

        return Integer.parseInt(result.toString());

    }

    public static void main(String[] args) {
        System.out.println("小数点前整数:" + getRateDigit(4.2));
        System.out.println("小数点后整数:" + getRateDecimal(4.2));
        System.out.println("随机数******************************************");
        for (int i = 0; i < 3; i++) {
            System.out.println("100到500内整数:" + rangeRandom(100, 500));
            System.out.println("小数点后整数:" + rangeRandom(10000000000L, 50000000000L));
        }
    }

    /**
     * 获得number中的前几位或者后几位     根据isForward来判断
     * @param number
     * @param digit
     * @param isForward
     * @return
     */
    public static String getNumber(int number, int digit, boolean isForward) {
        String numberStr = number + "";
        if (numberStr.length() > digit) {
            if (isForward) {
                return numberStr.substring(0, digit);
            } else {
                String subNumber = numberStr.substring(numberStr.length() - digit,
                    numberStr.length());

                int i = 1;
                while (subNumber.equals("0")) {
                    int beginIndex = numberStr.length() - digit - i;
                    int endIndex = numberStr.length() - i;

                    if (beginIndex >= 0 && endIndex >= 0) {
                        subNumber = numberStr.substring(beginIndex, endIndex);
                        i++;
                    }
                }
                if (!subNumber.equals("0")) {
                    return subNumber;
                } else {
                    return i + "";
                }
            }
        } else if (numberStr.length() < digit) {
            StringBuilder numberBuilder = new StringBuilder(digit);
            for (int i = 0, n = digit - numberStr.length(); i < n; i++) {
                numberBuilder.append(0);
            }
            return numberBuilder.append(number).toString();
        } else {
            return numberStr;
        }
    }
}
