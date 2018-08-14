package com.sleepwalker.dingdong.stock;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleepwalker.utils.HttpClientUtil;

public class LiangYeeStockData {

    public static void main(String[] args) {
        System.out.println(getStock("600805"));
    }

    public static Stock getStock(String symbol) {

        String url = "http://stock.liangyee.com/bus-api/stock/freeStockMarketData/getDailyKBar";

        String result = HttpClientUtil.get(url, getNameValuePairs(symbol));

        JSONObject jsonObject = JSON.parseObject(result);

        if (jsonObject.getString("message").equals("success")
            && jsonObject.getJSONArray("result").size() > 0) {
            String stockDataStr = (String) jsonObject.getJSONArray("result").get(0);

            String[] stockDataArr = stockDataStr.split(",");

            Stock stock = new Stock();
            stock.setSymbol(symbol);
            stock.setTradeDay(toTimestamp(stockDataArr[0]));
            stock.setOpenPrice(string2Double(stockDataArr[1]));
            stock.setClosePrice(string2Double(stockDataArr[2]));
            stock.setHighestPrice(string2Double(stockDataArr[3]));
            stock.setLowestPrice(string2Double(stockDataArr[4]));
            stock.setChg(stockDataArr[6] + "%");
            return stock;
        } else {
            System.out.println(symbol + " fail");
        }
        return null;
    }

    private static double string2Double(String str) {
        return org.apache.commons.lang.math.NumberUtils.toDouble(str);
    }

    private static List<NameValuePair> getNameValuePairs(String symbol) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("userKey", "f2e29ead13454620a530ec56e3231c28"));
        nameValuePairs.add(new BasicNameValuePair("symbol", symbol));
        nameValuePairs.add(new BasicNameValuePair("startDate", getToday()));
        nameValuePairs.add(new BasicNameValuePair("endDate", getToday()));

        if (symbol.startsWith("6")) {
            nameValuePairs.add(new BasicNameValuePair("type", "0"));
        } else {
            nameValuePairs.add(new BasicNameValuePair("type", "1"));
        }
        return nameValuePairs;
    }

    private static String getToday() {
        return new Timestamp(System.currentTimeMillis()).toString().substring(0, 10);
    }

    private static Timestamp toTimestamp(String dayStr) {
        return Timestamp.valueOf(dayStr + " 00:00:00");
    }
}
