package com.sleepwalker.dingdong.analyze;

import java.util.List;

import org.apache.http.NameValuePair;

import com.sleepwalker.dingdong.video.model.VideoSource;

public interface AnalyzeService {

    List<VideoSource> analyse(String url, List<NameValuePair> nameValuePairs);
}
