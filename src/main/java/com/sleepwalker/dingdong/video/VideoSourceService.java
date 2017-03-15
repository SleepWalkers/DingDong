package com.sleepwalker.dingdong.video;

import java.util.List;

import com.sleepwalker.dingdong.video.model.VideoSource;
import com.sleepwalker.dingdong.video.model.VideoSource.VideoType;

public interface VideoSourceService {

    void add(VideoSource videoSource);

    void add(List<VideoSource> videoSources);

    void add(int videoId, List<VideoSource> videoSources);

    VideoSource getLast(VideoType videoType);

    List<VideoSource> get(VideoType videoType, int page, int pageSize);
}
