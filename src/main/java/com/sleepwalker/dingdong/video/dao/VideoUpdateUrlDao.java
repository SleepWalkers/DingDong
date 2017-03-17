package com.sleepwalker.dingdong.video.dao;

import org.apache.ibatis.annotations.Param;

import com.sleepwalker.dingdong.video.model.VideoUpdateUrl;

public interface VideoUpdateUrlDao {
    void insert(VideoUpdateUrl videoUpdateUrl);

    void updateById(VideoUpdateUrl videoUpdateUrl);

    void deleteById(int id);

    VideoUpdateUrl selectById(int id);

    VideoUpdateUrl selectByVideoId(@Param("videoId") int videoId);
}