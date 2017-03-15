package com.sleepwalker.common.exception;

public class PageNotFoundException extends BizException {

    /** @author Sleepwalker 2016年12月13日 上午8:25:46 */
    private static final long   serialVersionUID  = -7456558807254365853L;
    private static final String DEFAULT_ERROR_MSG = "页面不存在";

    public PageNotFoundException() {
        super(DEFAULT_ERROR_MSG);
    }

    public PageNotFoundException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public PageNotFoundException(String msg) {
        super(PAGE_NOT_FOUND_CODE, msg);
    }
}
