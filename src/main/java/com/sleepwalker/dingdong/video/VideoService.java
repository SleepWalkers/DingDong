package com.sleepwalker.dingdong.video;

import java.util.List;

import com.sleepwalker.dingdong.video.model.Video;
import com.sleepwalker.dingdong.video.model.Video.VideoStatus;
import com.sleepwalker.dingdong.video.model.VideoUpdateUrl;

public interface VideoService {

    void add(Video video);

    void add(VideoUpdateUrl videoUpdateUrl);

    void add(Video video, VideoUpdateUrl videoUpdateUrl);

    void update(Video video);

    void update(VideoUpdateUrl videoUpdateUrl);

    Video get(int videoId);

    List<Video> get(int page, int pageSize);

    VideoUpdateUrl getVideoUpdateUrl(int videoId);

    List<VideoUpdateUrl> getVideoUpdateUrls(int dayOfWeek, VideoStatus videoStatus);
}
