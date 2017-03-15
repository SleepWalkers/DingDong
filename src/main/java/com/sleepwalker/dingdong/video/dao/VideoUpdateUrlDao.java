package com.sleepwalker.dingdong.video.dao;

import com.sleepwalker.dingdong.video.model.VideoUpdateUrl;

public interface VideoUpdateUrlDao {
    void insert(VideoUpdateUrl videoUpdateUrl);

    void updateById(VideoUpdateUrl videoUpdateUrl);

    void deleteById(int id);

    VideoUpdateUrl selectById(int id);
}