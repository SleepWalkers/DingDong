package com.sleepwalker.dingdong.video.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sleepwalker.dingdong.video.VideoSourceService;
import com.sleepwalker.dingdong.video.dao.VideoSourceDao;
import com.sleepwalker.dingdong.video.model.VideoSource;
import com.sleepwalker.dingdong.video.model.VideoSource.VideoType;
import com.sleepwalker.utils.PageUtil;

@Service("videoSourceService")
public class VideoSourceServiceImp implements VideoSourceService {

    @Resource
    private VideoSourceDao videoSourceDao;

    @Override
    public VideoSource getLast(VideoType videoType) {
        if (videoType == null) {
            return null;
        }
        List<VideoSource> videoSources = videoSourceDao.selectByType(videoType.getType(), 0, 1);
        if (videoSources.isEmpty()) {
            return null;
        }
        return videoSources.get(0);
    }

    @Override
    public List<VideoSource> get(VideoType videoType, int page, int pageSize) {
        if (videoType == null) {
            return null;
        }
        return videoSourceDao.selectByType(videoType.getType(), PageUtil.getStart(page, pageSize),
            PageUtil.getLimit(page, pageSize));
    }

    @Override
    public void add(VideoSource videoSource) {
        videoSourceDao.insert(videoSource);
    }

    @Override
    public void add(int videoId, List<VideoSource> videoSources) {
        if (videoSources == null) {
            return;
        }
        for (VideoSource videoSource : videoSources) {
            videoSource.setVideoId(videoId);
            add(videoSource);
        }
    }

    @Override
    public void add(List<VideoSource> videoSources) {
        if (videoSources == null) {
            return;
        }
        for (VideoSource videoSource : videoSources) {
            add(videoSource);
        }

    }

}
