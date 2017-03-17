package com.sleepwalker.dingdong.video;

import java.util.List;

import com.sleepwalker.dingdong.video.model.VideoSource;

public interface VideoSourceService {

    void add(VideoSource videoSource);

    void add(List<VideoSource> videoSources);

    void add(int videoId, List<VideoSource> videoSources);

    VideoSource getLast(int videoId);

    List<VideoSource> get(int videoId, int page, int pageSize);
}