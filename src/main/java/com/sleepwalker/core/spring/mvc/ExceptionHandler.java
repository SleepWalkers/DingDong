package com.sleepwalker.core.spring.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * 异常统一接口
 * @version $Id: ExceptionHandler.java, v 0.1 2014年3月6日 上午10:08:38 nero Exp $
 */
public interface ExceptionHandler {

    /**
     * 同步异常统一出口
     * @param request
     * @param response
     * @param exception
     * @return
     */
    ModelAndView resolveSyncException(HttpServletRequest request, HttpServletResponse response,
                                      Exception exception);

    /**
     * 异步异常统一出口
     * @param exception
     * @return
     */
    String resolveAsyncException(HttpServletRequest request, HttpServletResponse response,
                                 Exception exception);
}
