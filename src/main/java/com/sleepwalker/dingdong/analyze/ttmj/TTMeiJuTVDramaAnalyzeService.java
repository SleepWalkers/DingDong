package com.sleepwalker.dingdong.analyze.ttmj;

import java.util.List;

import org.apache.http.NameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleepwalker.dingdong.video.model.VideoSource.VideoType;
import com.sleepwalker.utils.HttpClientUtil;

@Service("ttMeiJuTVDramaAnalyzeService")
public class TTMeiJuTVDramaAnalyzeService extends TTMeiJuAnalyzeService {

    private static final String    JSON_HTML_KEY = "Html_Seedlist";

    private static final String    HTML_PRE      = "<html><head></head><body><table><tbody>";

    private static final String    HTML_SUF      = "</tbody></table></body></html>";

    private static final VideoType VIDEO_TYPE    = VideoType.TTMJ_TV_DRAMA;

    @Override
    protected Document getDocument(String url, List<NameValuePair> nameValuePairs) {
        String result = HttpClientUtil.post(url, nameValuePairs);
        JSONObject jsonObject = JSON.parseObject(result);
        return Jsoup.parse(HTML_PRE + jsonObject.get(JSON_HTML_KEY) + HTML_SUF);
    }

    @Override
    protected VideoType getVideoType() {
        return VIDEO_TYPE;
    }
}
