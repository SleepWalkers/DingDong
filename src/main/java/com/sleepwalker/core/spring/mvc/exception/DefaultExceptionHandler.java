package com.sleepwalker.core.spring.mvc.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.sleepwalker.core.spring.mvc.ExceptionHandler;

public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public ModelAndView resolveSyncException(HttpServletRequest request,
                                             HttpServletResponse response, Exception exception) {
        return null;
    }

    @Override
    public String resolveAsyncException(HttpServletRequest request, HttpServletResponse response,
                                        Exception exception) {
        return exception.getMessage();
    }

}
