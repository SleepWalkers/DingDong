package com.sleepwalker.core.spring.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public interface ControllerHandler {

    void preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception;

    void postSyncHandle(HttpServletRequest request, HttpServletResponse response,
                        ModelAndView modelAndView) throws Exception;

    void postAsyncHandle(HttpServletRequest request, HttpServletResponse response,
                         ModelAndView modelAndView) throws Exception;

}
