package com.sleepwalker.dingdong.video.model;

import java.sql.Timestamp;

import com.sleepwalker.utils.SecurityMD5Util;

public class VideoSource {
    private long      id;

    private int       videoId;

    private double    size;

    private String    url;

    private String    name;

    private String    format;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public enum VideoType {
                           TTMJ_MOVIE(1), TTMJ_TV_DRAMA(2);

        private int type;

        private VideoType(int type) {
            this.type = type;
        }

        public static VideoType get(int videoType) {
            for (VideoType type : VideoType.values()) {
                if (type.getType() == videoType) {
                    return type;
                }
            }
            return null;
        }

        public int getType() {
            return type;
        }
    }

    public String getUnicode() {
        return SecurityMD5Util.toMD5(videoId + name + url);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Timestamp getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Timestamp gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
}