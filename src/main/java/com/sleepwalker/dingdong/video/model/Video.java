package com.sleepwalker.dingdong.video.model;

import java.sql.Timestamp;

public class Video {
    private int       id;

    private String    name;

    private int       season;

    private int       episode;

    private int       status;

    private int       weekOfDay;

    private Timestamp returnTime;

    private Timestamp gmtCreated;

    private Timestamp gmtModified;

    public enum VideoStatus {
                             CASTING(1, "播出中"),

                             END_HALF_SEASON(2, "半季终"),

                             END_THIS_SEASON(3, "本季终"),

                             STOP(4, "停播"), FINISHED(5, "剧终"), KILLED(6, "被砍");

        private int    type; //数据库保存的数字

        private String desc; //中文描述

        private VideoStatus(int type, String desc) {
            this.desc = desc;
            this.type = type;
        }

        public int getType() {
            return this.type;
        }

        public String getDesc() {
            return this.desc;
        }

        public static VideoStatus castByDesc(String desc) {
            for (VideoStatus videoStatus : VideoStatus.values()) {
                if (videoStatus.getDesc().equals(desc)) {
                    return videoStatus;
                }
            }
            return null;
        }

        public static VideoStatus castByType(int type) {
            for (VideoStatus videoStatus : VideoStatus.values()) {
                if (videoStatus.getType() == type) {
                    return videoStatus;
                }
            }
            return null;
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWeekOfDay() {
        return weekOfDay;
    }

    public void setWeekOfDay(int weekOfDay) {
        this.weekOfDay = weekOfDay;
    }

    public Timestamp getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Timestamp returnTime) {
        this.returnTime = returnTime;
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
}