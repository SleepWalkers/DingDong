package com.sleepwalker.dingdong.cgtz;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sleepwalker.utils.HttpClientUtil;

public class Cgtz {

    public static void main(String[] args) {

        String urlTmp = "https://www.cgtz.com/project/index_t_t_4_2_0_0_%d.html";

        boolean stopLoop = false;
        for (int i = 1;; i++) {
            String url = String.format(urlTmp, i);
            String result = HttpClientUtil.post(url, null);
            Document rootDoc = Jsoup.parse(result);

            Elements elements = rootDoc.getElementsByClass("projectList");

            List<Element> saleHaves = elements.get(0).getElementsByClass("saleHave");

            if (stopLoop) {
                break;
            }
            for (Element element : saleHaves) {
                Elements elements2 = element.getElementsByClass("investBtn");

                String text = elements2.get(0).text();
                String loopUrl = elements2.get(0).attributes().get("href");
                Elements elements3 = element.getElementsByClass("longLi");

                List<Element> nodes = elements3.get(0).getAllElements();

                String days = nodes.get(1).text();

                if (nodes.size() < 4) {
                    continue;
                }
                String rateStr = nodes.get(3).text();
                rateStr = rateStr.replaceAll("折", "").trim();

                double rate = Double.parseDouble(rateStr);
                if (rate > 7) {
                    stopLoop = true;
                    break;
                }
                if (Integer.parseInt(days) > 30) {
                    continue;
                }
                if (text.contains("惠民贷") || text.contains("惠企贷")) {
                    continue;
                }

                days = days + "天";
                rateStr = rate + "%";
                loopUrl = "https://www.cgtz.com/project/" + loopUrl;
                System.out.println(text + " " + days + " " + rate);
                System.out.println(loopUrl);
                System.out.println("————————————————————————————————————————————");
            }
        }
    }

}
