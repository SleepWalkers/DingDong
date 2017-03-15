package com.sleepwalker.dingdong.video.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sleepwalker.dingdong.video.model.VideoSource;

public interface VideoSourceDao {
    void insert(VideoSource videoSource);

    void updateById(VideoSource videoSource);

    void deleteById(long id);

    VideoSource selectById(long id);

    List<VideoSource> selectByType(@Param("type") int type, @Param("start") int start,
                             @Param("limit") int limit);
}