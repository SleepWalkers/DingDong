package com.sleepwalker.utils;

import java.math.BigDecimal;

public class PriceUtil {
    /**
     * 通过折扣计算价格
     *
     * @param unitPrice 单价
     * @param discount 折扣(例:8.8折)
     * @return 打折后的价格
     */
    public static double calcPrice(double unitPrice, double discount) {
        double price = unitPrice * discount / 10;
        BigDecimal bg = new BigDecimal(price);
        price = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return price;
    }

    /**
     * 通过折扣计算价格
     *
     * @param unitPrice 单价
     * @param discount 折扣(例:8.8折)
     * @param quantity 数量
     * @return 总价
     */
    public static double calcPrice(double unitPrice, double discount, int quantity) {
        double price = unitPrice * quantity * discount / 10;
        BigDecimal bg = new BigDecimal(price);
        price = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return price;
    }

    public static double calcuPrice(double price, int quantity) {
        BigDecimal bg = new BigDecimal(price * quantity);
        price = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return price;
    }
}
