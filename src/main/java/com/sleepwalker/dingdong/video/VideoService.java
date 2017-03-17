package com.sleepwalker.dingdong.video;

import com.sleepwalker.dingdong.video.model.Video;
import com.sleepwalker.dingdong.video.model.VideoUpdateUrl;

public interface VideoService {

    void add(Video video);

    void add(VideoUpdateUrl videoUpdateUrl);

    void add(Video video, VideoUpdateUrl videoUpdateUrl);

    VideoUpdateUrl getVideoUpdateUrl(int videoId);
}
