package com.sleepwalker.dingdong.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.sleepwalker.utils.HttpClientUtil;

public class StockInfoAnalyzer {

    private static final String  re1     = "（";                            // Any Single Character 1

    private static final String  re2     = "(\\d+)";                       // Integer Number 1

    private static final String  re3     = "）";                            // Any Single Character 2

    private static final Pattern PATTERN = Pattern.compile(re1 + re2 + re3,
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static void main(String[] args) {

        String url = "https://mp.weixin.qq.com/s/di1AW0sDyZwvvcKTEfErng";

        String result = HttpClientUtil.get(url, null);
        Document rootDoc = Jsoup.parse(result);

        Element element = rootDoc.getElementById("js_content");

        Matcher m = PATTERN.matcher(element.text());

        List<String> symbols = new ArrayList<>();
        while (m.find()) {
            String c1 = m.group(1);
            symbols.add(c1);
        }

        try {
            for (String symbol : symbols) {
                System.out.println(LiangYeeStockData.getStock(symbol));
                Thread.sleep(1 * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
