package com.sleepwalker.dingdong.web.ttmj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sleepwalker.dingdong.analyze.AnalyzeService;
import com.sleepwalker.dingdong.video.VideoService;
import com.sleepwalker.dingdong.video.VideoSourceService;
import com.sleepwalker.dingdong.video.model.Video;
import com.sleepwalker.dingdong.video.model.Video.VideoStatus;
import com.sleepwalker.dingdong.video.model.VideoSource;
import com.sleepwalker.dingdong.video.model.VideoUpdateUrl;

@Controller
public class TTMeiJuTaskController {

    @Resource
    private VideoService         videoService;

    @Resource
    private VideoSourceService   videoSourceService;

    @Resource
    private AnalyzeService       ttMeiJuMovieAnalyzeService;

    @Resource
    private AnalyzeService       ttMeiJuTVDramaAnalyzeService;

    private static final String  SEASON_RE                  = ".*\\s+S(\\d+)E(\\d+)";

    private static final Pattern SEASON_AND_EPSIODE_PATTERN = Pattern.compile(SEASON_RE,
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final Logger  logger                     = Logger
        .getLogger(TTMeiJuTaskController.class);

    @Scheduled(cron = "0 0 0/1 * * ?")
    @ResponseBody
    @RequestMapping(value = "/api/spider/video/ttmeiju/movie", method = RequestMethod.GET)
    public void movieUpdateTask() {
        VideoSource lastVideo = videoSourceService.getLast(1);

        VideoUpdateUrl movieUpdateUrl = videoService.getVideoUpdateUrl(1);

        if (movieUpdateUrl == null) {
            return;
        }
        String updateUrl = movieUpdateUrl.getUpdateUrl();

        List<VideoSource> newVideos = ttMeiJuMovieAnalyzeService.analyse(updateUrl, null);
        if (newVideos == null || newVideos.isEmpty()) {
            return;
        }

        for (int i = 0; i < newVideos.size(); i++) {
            if (newVideos.get(i).getUnicode().equals(lastVideo.getUnicode())) {
                newVideos = newVideos.subList(0, i);
                break;
            }
        }
        Collections.reverse(newVideos);
        videoSourceService.add(1, newVideos);
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    @ResponseBody
    @RequestMapping(value = "/api/spider/video/ttmeiju/drama", method = RequestMethod.GET)
    public void tvDramaUpdateTask() {

        List<VideoUpdateUrl> videoUpdateUrls = videoService.getVideoUpdateUrls(
            new DateTime(System.currentTimeMillis()).getDayOfWeek(), VideoStatus.CASTING);
        if (videoUpdateUrls == null || videoUpdateUrls.isEmpty()) {
            return;
        }
        for (VideoUpdateUrl videoUpdateUrl : videoUpdateUrls) {
            if (StringUtils.isNotBlank(videoUpdateUrl.getUpdateUrl())) {
                Video video = videoService.get(videoUpdateUrl.getVideoId());

                List<VideoSource> newVideos = ttMeiJuTVDramaAnalyzeService
                    .analyse(videoUpdateUrl.getUpdateUrl(), videoUpdateUrl.getNameValuePair());

                logger.info(video.getName());
                if (newVideos == null || newVideos.isEmpty()) {
                    continue;
                }

                for (int i = 0; i < newVideos.size(); i++) {
                    if (getEpsiode(newVideos.get(i).getName()) <= video.getEpisode()) {
                        newVideos = newVideos.subList(0, i);
                        break;
                    }
                }
                if (newVideos == null || newVideos.isEmpty()) {
                    continue;
                }
                Collections.reverse(newVideos);
                videoSourceService.add(video.getId(), newVideos);

                video.setEpisode(getEpsiode(newVideos.get(newVideos.size() - 1).getName()));
                videoService.update(video);
                if (newSeason(video, videoUpdateUrl)) {

                    List<NameValuePair> nameValuePairs = new ArrayList<>();
                    nameValuePairs.add(new BasicNameValuePair("mid",
                        videoUpdateUrl.getParamsObject().getString("mid")));
                    nameValuePairs.add(new BasicNameValuePair("sid", (video.getSeason() + 1) + ""));

                    newVideos = ttMeiJuTVDramaAnalyzeService.analyse(videoUpdateUrl.getUpdateUrl(),
                        nameValuePairs);

                    Collections.reverse(newVideos);
                    videoSourceService.add(video.getId(), newVideos);

                    video.setSeason(video.getSeason() + 1);
                    video.setEpisode(getEpsiode(newVideos.get(newVideos.size() - 1).getName()));
                    videoService.update(video);

                    Map<String, String> newParamsMap = new HashMap<>();
                    newParamsMap.put("mid", videoUpdateUrl.getParamsObject().getString("mid"));
                    newParamsMap.put("sid", video.getSeason() + "");

                    videoUpdateUrl.setUpdateParams(JSON.toJSONString(newParamsMap));
                    videoService.update(videoUpdateUrl);
                }
            }
        }
    }

    private int getSeason(String name) {
        Matcher m = SEASON_AND_EPSIODE_PATTERN.matcher(name);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return -1;
    }

    private int getEpsiode(String name) {
        Matcher m = SEASON_AND_EPSIODE_PATTERN.matcher(name);
        if (m.find()) {
            return Integer.parseInt(m.group(2));
        }
        return -1;
    }

    private boolean newSeason(Video video, VideoUpdateUrl videoUpdateUrl) {
        if (videoUpdateUrl.getParamsObject() == null) {
            return false;
        }
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs
            .add(new BasicNameValuePair("mid", videoUpdateUrl.getParamsObject().getString("mid")));
        nameValuePairs.add(new BasicNameValuePair("sid", (video.getSeason() + 1) + ""));

        List<VideoSource> newVideos = ttMeiJuTVDramaAnalyzeService
            .analyse(videoUpdateUrl.getUpdateUrl(), nameValuePairs);

        return newVideos != null && !newVideos.isEmpty();
    }
}
