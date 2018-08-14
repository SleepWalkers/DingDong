package com.sleepwalker.dingdong.stock;

import java.sql.Timestamp;
import java.text.DecimalFormat;

public class Stock {

    private String                     symbol;

    private Timestamp                  tradeDay;

    private double                     openPrice;

    private double                     closePrice;

    private double                     highestPrice;

    private double                     lowestPrice;

    private String                     chg;

    private static final DecimalFormat df = new DecimalFormat("00.00");

    public Timestamp getTradeDay() {
        return tradeDay;
    }

    public void setTradeDay(Timestamp tradeDay) {
        this.tradeDay = tradeDay;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getChg() {
        return chg;
    }

    public void setChg(String chg) {
        this.chg = chg;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol + " " + formatNumber(openPrice) + " " + formatNumber(lowestPrice) + " "
               + formatNumber(highestPrice) + " " + formatNumber(closePrice) + " " + chg;
    }

    private String formatNumber(double number) {
        String numberStr = df.format(openPrice);

        if (numberStr.startsWith("0")) {
            numberStr = " " + numberStr.substring(1);
        }
        return numberStr;
    }
}
