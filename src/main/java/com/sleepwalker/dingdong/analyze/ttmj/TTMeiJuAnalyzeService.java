package com.sleepwalker.dingdong.analyze.ttmj;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.sleepwalker.dingdong.analyze.HtmlAnalyzeService;
import com.sleepwalker.dingdong.video.model.VideoSource;
import com.sleepwalker.dingdong.video.model.VideoSource.VideoType;

public abstract class TTMeiJuAnalyzeService extends HtmlAnalyzeService {

    private static final Logger logger = Logger.getLogger(TTMeiJuAnalyzeService.class);

    @Override
    public List<VideoSource> analyse(String url, List<NameValuePair> nameValuePairs) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        Document document = getDocument(url, nameValuePairs);
        if (document == null) {
            return null;
        }
        Elements elements = document.getElementsByClass("Scontent");
        if (elements == null) {
            return null;
        }

        return buildVideos(elements);
    }

    protected abstract Document getDocument(String url, List<NameValuePair> nameValuePairs);

    protected List<VideoSource> buildVideos(Elements elements) {
        List<VideoSource> videoSources = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            Elements trElements = getFieldElements(elements.get(i));
            VideoSource videoSource = buildVideo(trElements);
            if (videoSource == null || StringUtils.isBlank(videoSource.getUrl())) {
                continue;
            }
            videoSources.add(videoSource);
        }
        return videoSources;

    }

    protected VideoSource buildVideo(Elements dataElements) {
        VideoSource videoSource = new VideoSource();
        videoSource.setName(getName(dataElements));
        videoSource.setUrl(getUrl(dataElements));
        videoSource.setSize(getSize(dataElements));
        videoSource.setFormat(getFormat(dataElements));
        return videoSource;
    }

    protected abstract VideoType getVideoType();

    protected Elements getFieldElements(Element element) {
        return element.getElementsByTag("td");
    }

    protected String getFormat(Elements elements) {
        Element formatElement = elements.get(5);
        return formatElement.text();
    }

    protected double getSize(Elements elements) {
        Element sizeElement = elements.get(4);
        String sizeStr = sizeElement.text();

        if (StringUtils.isBlank(sizeStr)) {
            return 0;
        }

        String doubleStr = sizeStr.substring(0, sizeStr.length() - 1);
        if (StringUtils.isBlank(doubleStr)) {
            return 0;
        }
        double size = 0;
        try {
            size = Double.parseDouble(doubleStr);
        } catch (NumberFormatException e) {
            logger.error(doubleStr);
        }
        if (sizeStr.endsWith("G") || sizeStr.endsWith("g")) {
            return size * 1024;
        }
        return size;
    }

    protected String getUrl(Elements elements) {
        Element urlElement = elements.get(3);
        List<Node> urlNodes = urlElement.childNodes();

        for (Node node : urlNodes) {
            String nodeText = node.attr("href").toString();
            if (StringUtils.isBlank(nodeText)) {
                continue;
            }
            if (nodeText.contains("magnet:?")) {
                return nodeText;
            }
            if (nodeText.contains("ed2k:")) {
                return nodeText;
            }
        }
        return null;
    }

    protected String getName(Elements elements) {
        Element nameElement = elements.get(2);
        Node chileNode = nameElement.childNode(0);
        List<Node> chilechileNode = chileNode.childNodes();
        return chilechileNode.get(0).toString();
    }

}
