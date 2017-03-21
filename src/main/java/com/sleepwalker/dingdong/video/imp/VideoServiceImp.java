package com.sleepwalker.dingdong.video.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sleepwalker.dingdong.video.VideoService;
import com.sleepwalker.dingdong.video.dao.VideoDao;
import com.sleepwalker.dingdong.video.dao.VideoUpdateUrlDao;
import com.sleepwalker.dingdong.video.model.Video;
import com.sleepwalker.dingdong.video.model.Video.VideoStatus;
import com.sleepwalker.dingdong.video.model.VideoUpdateUrl;
import com.sleepwalker.utils.PageUtil;

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

    @Override
    public List<VideoUpdateUrl> getVideoUpdateUrls(int dayOfWeek, VideoStatus videoStatus) {
        if (dayOfWeek <= 0 || videoStatus == null) {
            return null;
        }
        return videoUpdateUrlDao.selectByDayAndStatus(dayOfWeek, videoStatus.getType());
    }

    @Override
    public List<Video> get(int page, int pageSize) {
        return videoDao.selectByLimit(PageUtil.getStart(page, pageSize),
            PageUtil.getLimit(page, pageSize));
    }

    @Override
    public void update(Video video) {
        videoDao.updateById(video);
    }

    @Override
    public Video get(int videoId) {
        if (videoId <= 0) {
            return null;
        }
        return videoDao.selectById(videoId);
    }

    @Override
    public void update(VideoUpdateUrl videoUpdateUrl) {
        if (videoUpdateUrl == null) {
            return;
        }
        videoUpdateUrlDao.updateById(videoUpdateUrl);
    }

}
