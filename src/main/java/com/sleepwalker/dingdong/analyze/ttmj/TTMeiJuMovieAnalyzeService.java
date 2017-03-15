package com.sleepwalker.dingdong.analyze.ttmj;

import java.util.List;

import org.apache.http.NameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.sleepwalker.dingdong.video.model.VideoSource.VideoType;
import com.sleepwalker.utils.HttpClientUtil;

@Service("ttMeiJuMovieAnalyzeService")
public class TTMeiJuMovieAnalyzeService extends TTMeiJuAnalyzeService {

    private static final VideoType VIDEO_TYPE = VideoType.TTMJ_MOVIE;

    @Override
    protected Document getDocument(String url, List<NameValuePair> nameValuePairs) {
        String result = HttpClientUtil.get(url, nameValuePairs);
        return Jsoup.parse(result);
    }

    @Override
    protected VideoType getVideoType() {
        return VIDEO_TYPE;
    }
}
