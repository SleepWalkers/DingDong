package com.sleepwalker.dingdong.video.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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

    public JSONObject getParamsObject() {
        if (StringUtils.isBlank(getUpdateParams())) {
            return null;
        }
        return JSON.parseObject(getUpdateParams());
    }

    public List<NameValuePair> getNameValuePair() {
        if (StringUtils.isBlank(getUpdateParams())) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(getUpdateParams());
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (String key : jsonObject.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, jsonObject.getString(key)));
        }
        return nameValuePairs;
    }

    public String getUpdateParams() {
        return updateParams;
    }

    public void setUpdateParams(String updateParams) {
        this.updateParams = updateParams;
    }
}