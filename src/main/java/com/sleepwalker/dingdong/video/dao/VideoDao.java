package com.sleepwalker.dingdong.video.dao;

import com.sleepwalker.dingdong.video.model.Video;

public interface VideoDao {
    void insert(Video video);

    void updateById(Video video);

    void deleteById(int id);

    Video selectById(int id);
}