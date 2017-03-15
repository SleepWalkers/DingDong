package com.sleepwalker.core.spring.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.sleepwalker.core.spring.mvc.ControllerHandler;

public class DefaultControllerHandler implements ControllerHandler {

    @Override
    public void postSyncHandle(HttpServletRequest request, HttpServletResponse response,
                               ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void postAsyncHandle(HttpServletRequest request, HttpServletResponse response,
                                ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response)
                                                                                   throws Exception {
    }

}
