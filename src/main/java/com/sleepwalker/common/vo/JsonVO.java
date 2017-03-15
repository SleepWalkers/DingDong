package com.sleepwalker.common.vo;

import com.alibaba.fastjson.JSON;

public class JsonVO {

    /**
     * 返回状态，是否正常
     */
    private boolean isSuccess;

    /**
     * 是否需要重定向
     */
    private boolean isRedirect;

    /**
     * 返回信息
     */
    private String  msg;

    /**
     * 重定向地址(UI)
     */
    private String  redirectURL;

    /**
     * 数据体
     */
    private Object  data;

    public JsonVO() {
    }

    public JsonVO(boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.isRedirect = false;
    }

    public JsonVO(boolean isSuccess, boolean isRedirect, String redirectUrl) {
        this.isSuccess = isSuccess;
        this.isRedirect = isRedirect;
        this.redirectURL = redirectUrl;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static void main(String[] args) {
        JsonVO jsonVO = new JsonVO();
        jsonVO.setIsSuccess(false);
        jsonVO.setMsg("错误信息");
        jsonVO.setIsRedirect(true);
        jsonVO.setRedirectURL("http://www.123.com");
        jsonVO.setData("data");
        System.out.println(JSON.toJSONString(jsonVO));
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean getIsRedirect() {
        return isRedirect;
    }

    public void setIsRedirect(boolean isRedirect) {
        this.isRedirect = isRedirect;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
