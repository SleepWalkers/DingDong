package com.sleepwalker.dingdong.web.ttmj;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sleepwalker.common.vo.JsonVO;
import com.sleepwalker.dingdong.analyze.AnalyzeService;
import com.sleepwalker.dingdong.video.VideoService;
import com.sleepwalker.dingdong.video.VideoSourceService;
import com.sleepwalker.dingdong.video.model.Video;
import com.sleepwalker.dingdong.video.model.Video.VideoStatus;
import com.sleepwalker.dingdong.video.model.VideoSource;
import com.sleepwalker.dingdong.video.model.VideoUpdateUrl;
import com.sleepwalker.utils.HttpClientUtil;

@Controller
public class TTMeiJuSpiderController {

    @Resource
    private VideoService         videoService;

    @Resource
    private VideoSourceService   videoSourceService;

    @Resource
    private AnalyzeService       ttMeiJuMovieAnalyzeService;

    @Resource
    private AnalyzeService       ttMeiJuTVDramaAnalyzeService;

    private static final Logger  logger                     = Logger
        .getLogger(TTMeiJuSpiderController.class);

    private static final String  OUT_VIDEO_ID_RE_1          = "<script src=\"\\/index.php\\/user\\/rss_status\\/mid\\/";
    private static final String  OUT_VIDEO_ID_RE_2          = "(\\d+)";
    private static final String  OUT_VIDEO_ID_RE_3          = "\\.html\"";

    private static final String  DOMAIN                     = "http://www.ttmeiju.com";

    private static final String  VIDEO_LIST_URL             = DOMAIN
                                                              + "/index.php/summary/index/p/%s.html";

    private static final String  MOVIE_START_URL            = "http://www.ttmeiju.com/index.php/meiju/index/engename/Movie/p/%s.html";

    private static final String  TV_DRAMA_URL               = "http://www.ttmeiju.com/index.php/meiju/get_episodies.html";

    private static final String  SEASON_RE                  = ".*\\s+S(\\d+)E(\\d+)";

    private static final Pattern SEASON_AND_EPSIODE_PATTERN = Pattern.compile(SEASON_RE,
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final Pattern OUT_VIDEO_ID_PATTERN       = Pattern.compile(
        OUT_VIDEO_ID_RE_1 + OUT_VIDEO_ID_RE_2 + OUT_VIDEO_ID_RE_3,
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    @ResponseBody
    @RequestMapping(value = "/api/spider/video/ttmeiju/loadList", method = RequestMethod.GET)
    public String ttMeiJuLoadList() throws IOException {
        //        boolean firstLoop = true;
        //        int index = 10;
        //        for (int i = 1; i > 0; i--) {
        //            String url = String.format(VIDEO_LIST_URL, i);
        //            if (!firstLoop) {
        //                index = 1;
        //            } else {
        //                firstLoop = false;
        //            }
        //            loadList(index, url);
        //            logger.info("抓取第" + i + "页");
        //        }

        String[] urlArr = { DOMAIN + "/meiju/Game.of.Thrones.html",
                            DOMAIN + "/meiju/The.Big.Bang.Theory.html",
                            DOMAIN + "/meiju/THE.Walking.Dead.html" };
        int[] videoIdArr = { 1164, 1165, 1166 };
        for (int i = 0; i < urlArr.length; i++) {
            loadDetail(videoIdArr[i], urlArr[i]);
        }
        return new JsonVO(true).toString();
    }

    private void loadList(int index, String url) throws IOException {
        List<Element> videoElements = getVideoElements(index, url);
        for (Element videoElement : videoElements) {
            List<Node> nodes = videoElement.childNodes();
            Node nameAndUrlNode = nodes.get(3).childNode(0);
            String detailUrl = DOMAIN + nameAndUrlNode.attr("href").toString();

            if (detailUrl.equals("http://www.ttmeiju.com/meiju/Movie.html")) {
                continue;
            }
            Video video = new Video();
            video.setName(getName(nodes));
            video.setStatus(getVideoStatus(nodes));
            video.setWeekOfDay(getWeekOfDay(nodes));
            video.setReturnTime(getReturnTime(nodes));

            String html = HttpClientUtil.get(detailUrl, null);

            int outVideoId = getOutVideoId(html);
            List<Integer> seasons = getSeasons(html);

            if (outVideoId == 0 || seasons == null || seasons.isEmpty()) {
                continue;
            }
            video.setSeason(seasons.get(0));

            VideoUpdateUrl videoUpdateUrl = new VideoUpdateUrl();
            videoUpdateUrl.setFromSiteId(1);
            videoUpdateUrl.setUpdateUrl(TV_DRAMA_URL);

            Map<String, Integer> paramMap = new HashMap<>();
            paramMap.put("sid", seasons.get(0));
            paramMap.put("mid", outVideoId);
            videoUpdateUrl.setUpdateParams(JSON.toJSONString(paramMap));

            videoService.add(video, videoUpdateUrl);

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            Collections.reverse(seasons);
            for (int season : seasons) {
                nameValuePairs.clear();
                nameValuePairs.add(new BasicNameValuePair("sid", season + ""));
                nameValuePairs.add(new BasicNameValuePair("mid", outVideoId + ""));
                List<VideoSource> newVideoSources = ttMeiJuTVDramaAnalyzeService
                    .analyse(TV_DRAMA_URL, nameValuePairs);
                if (newVideoSources == null || newVideoSources.isEmpty()) {
                    continue;
                }
                Collections.reverse(newVideoSources);
                videoSourceService.add(video.getId(), newVideoSources);
                logger.info("mid=" + outVideoId + ",sid=" + season);
            }
        }
    }

    private void loadDetail(int videoId, String detailUrl) {

        String html = HttpClientUtil.get(detailUrl, null);

        int outVideoId = getOutVideoId(html);
        List<Integer> seasons = getSeasons(html);

        if (outVideoId == 0 || seasons == null || seasons.isEmpty()) {
            return;
        }

        VideoUpdateUrl videoUpdateUrl = new VideoUpdateUrl();
        videoUpdateUrl.setFromSiteId(1);
        videoUpdateUrl.setUpdateUrl(TV_DRAMA_URL);

        Map<String, Integer> paramMap = new HashMap<>();
        paramMap.put("sid", seasons.get(0));
        paramMap.put("mid", outVideoId);
        videoUpdateUrl.setUpdateParams(JSON.toJSONString(paramMap));

        videoUpdateUrl.setVideoId(videoId);
        videoService.add(videoUpdateUrl);

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Collections.reverse(seasons);
        for (int season : seasons) {
            nameValuePairs.clear();
            nameValuePairs.add(new BasicNameValuePair("sid", season + ""));
            nameValuePairs.add(new BasicNameValuePair("mid", outVideoId + ""));
            List<VideoSource> newVideoSources = ttMeiJuTVDramaAnalyzeService.analyse(TV_DRAMA_URL,
                nameValuePairs);
            if (newVideoSources == null || newVideoSources.isEmpty()) {
                continue;
            }
            Collections.reverse(newVideoSources);
            videoSourceService.add(videoId, newVideoSources);
            logger.info("mid=" + outVideoId + ",sid=" + season);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/api/spider/video/ttmeiju/tvDrama", method = RequestMethod.GET)
    public String ttMeiJuTvDramaSpider() {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (int i = 0; i < 1200; i++) {
            for (int j = 0; j < 15; j++) {
                nameValuePairs.clear();
                nameValuePairs.add(new BasicNameValuePair("sid", j + ""));
                nameValuePairs.add(new BasicNameValuePair("mid", i + ""));
                String url = String.format(MOVIE_START_URL, j, i);
                List<VideoSource> newVideos = ttMeiJuMovieAnalyzeService.analyse(url,
                    nameValuePairs);
                videoSourceService.add(newVideos);

                logger.info("mid=" + i + ",sid=" + j);
            }
        }
        return new JsonVO(true).toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/spider/video/ttmeiju/updateSeasonAndEpisode", method = RequestMethod.GET)
    public void updateE() {

        int page = 1;
        int pageSize = 20;
        List<Video> videos = videoService.get(page, pageSize);
        videos.remove(0);
        do {
            for (Video video : videos) {
                updateSeasonAndEpisode(video);
            }
            page++;
            videos = videoService.get(page, pageSize);
        } while (videos != null && !videos.isEmpty());
    }

    private void updateSeasonAndEpisode(Video video) {

        int page = 1;
        int pageSize = 20;
        List<VideoSource> videoSources = videoSourceService.get(video.getId(), page, pageSize);

        int maxSeason = video.getSeason();
        int maxEpisode = 1;
        do {
            for (VideoSource videoSource : videoSources) {
                Matcher m = SEASON_AND_EPSIODE_PATTERN.matcher(videoSource.getName());
                logger.info(video.getName());
                if (m.find()) {
                    logger.info("season: " + m.group(1) + " episode: " + m.group(2));
                    if (Integer.parseInt(m.group(1)) == maxSeason) {
                        if (Integer.parseInt(m.group(2)) > maxEpisode) {
                            maxEpisode = Integer.parseInt(m.group(2));
                        }
                    } else if (Integer.parseInt(m.group(1)) > maxSeason) {
                        maxSeason = Integer.parseInt(m.group(1));
                        maxEpisode = 1;
                        if (Integer.parseInt(m.group(2)) > maxEpisode) {
                            maxEpisode = Integer.parseInt(m.group(2));
                        }
                    }
                }
            }
            page++;
            videoSources = videoSourceService.get(video.getId(), page, pageSize);
        } while (videoSources != null && !videoSources.isEmpty());

        video.setEpisode(maxEpisode);
        video.setSeason(maxSeason);
        videoService.update(video);
    }

    private List<Integer> getSeasons(String html) {

        Document document = Jsoup.parse(html);
        Element element = document.getElementById("seasoninfo");
        if (element == null) {
            return null;
        }
        Elements seasonElements = element.getElementsByTag("h3");

        if (seasonElements.isEmpty()) {
            return null;
        }
        List<Integer> seasons = new ArrayList<>();

        for (Element seasonElement : seasonElements) {
            seasons.add(Integer.parseInt(seasonElement.id()));
        }
        return seasons;
    }

    private int getOutVideoId(String html) {
        Matcher m = OUT_VIDEO_ID_PATTERN.matcher(html);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

    private String getName(List<Node> nodes) {
        Node nameAndUrlNode = nodes.get(3).childNode(0);
        return nameAndUrlNode.childNode(0).toString();
    }

    private int getVideoStatus(List<Node> nodes) {
        String status = null;
        Node statusNode = nodes.get(5).childNode(0);
        if (statusNode.childNodeSize() >= 1) {
            status = statusNode.childNode(0).toString();
        } else {
            status = statusNode.toString();
        }
        VideoStatus videoStatus = VideoStatus.castByDesc(status);
        if (videoStatus == null) {
            return 0;
        }
        return videoStatus.getType();
    }

    private List<Element> getVideoElements(int index, String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        Elements tableElements = document.getElementsByTag("table").get(0).getElementsByTag("tr");
        List<Element> elements = tableElements.subList(index, tableElements.size() - 1);
        return elements;
    }

    private Timestamp getReturnTime(List<Node> nodes) {
        String returnTime = nodes.get(9).childNode(0).toString().trim();

        if (returnTime.equals("暂无")) {
            return null;
        } else {
            try {
                return Timestamp.valueOf(returnTime + " 00:00:00");
            } catch (Exception e) {
                logger.error("", e);
            }
            return null;
        }
    }

    private int getWeekOfDay(List<Node> nodes) {

        String weekOfDay = nodes.get(7).childNode(0).toString().trim();
        if (weekOfDay.equals("暂无")) {
            return 0;
        } else if (weekOfDay.equals("每周一")) {
            return 1;
        } else if (weekOfDay.equals("每周二")) {
            return 2;
        } else if (weekOfDay.equals("每周三")) {
            return 3;
        } else if (weekOfDay.equals("每周四")) {
            return 4;
        } else if (weekOfDay.equals("每周五")) {
            return 5;
        } else if (weekOfDay.equals("每周六")) {
            return 6;
        } else if (weekOfDay.equals("每周日")) {
            return 7;
        } else {
            return -1;
        }
    }
}
