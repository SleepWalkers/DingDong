package com.sleepwalker.dingdong.video.model;

import java.sql.Timestamp;

public class VideoUpdateUrl {
    private long      id;

    private int       videoId;

    private int       fromSiteId;

    private String    updateUrl;

    private String    updateParams;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
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

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public int getFromSiteId() {
        return fromSiteId;
    }

    public void setFromSiteId(int fromSiteId) {
        this.fromSiteId = fromSiteId;
    }

    public String getUpdateParams() {
        return updateParams;
    }

    public void setUpdateParams(String updateParams) {
        this.updateParams = updateParams;
    }
}