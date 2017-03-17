package com.sleepwalker.dingdong.video.imp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sleepwalker.dingdong.video.VideoService;
import com.sleepwalker.dingdong.video.dao.VideoDao;
import com.sleepwalker.dingdong.video.dao.VideoUpdateUrlDao;
import com.sleepwalker.dingdong.video.model.Video;
import com.sleepwalker.dingdong.video.model.VideoUpdateUrl;

@Service("videoService")
public class VideoServiceImp implements VideoService {

    @Resource
    private VideoDao          videoDao;

    @Resource
    private VideoUpdateUrlDao videoUpdateUrlDao;

    @Override
    public void add(Video video) {
        if (video == null) {
            return;
        }
        videoDao.insert(video);
    }

    @Override
    public void add(VideoUpdateUrl videoUpdateUrl) {
        if (videoUpdateUrl == null) {
            return;
        }
        videoUpdateUrlDao.insert(videoUpdateUrl);
    }

    @Override
    public void add(Video video, VideoUpdateUrl videoUpdateUrl) {
        if (video == null || videoUpdateUrl == null) {
            return;
        }
        add(video);
        videoUpdateUrl.setVideoId(video.getId());
        videoUpdateUrlDao.insert(videoUpdateUrl);
    }

    @Override
    public VideoUpdateUrl getVideoUpdateUrl(int videoId) {
        if (videoId <= 0) {
            return null;
        }
        return videoUpdateUrlDao.selectByVideoId(videoId);
    }

}
