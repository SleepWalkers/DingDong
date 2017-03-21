package com.sleepwalker.dingdong.video.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sleepwalker.dingdong.video.model.Video;

public interface VideoDao {
    void insert(Video video);

    void updateById(Video video);

    void deleteById(int id);

    Video selectById(int id);

    List<Video> selectByLimit(@Param("start") int start, @Param("limit") int limit);
}