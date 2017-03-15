package com.sleepwalker.core.spring.mvc;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.util.TokenProcessor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 同步令牌拦截器，用于生成和检查同步令牌，校验接口是否为合法页面入口
 * @version $Id: MvcSyncTokenInterceptor.java, v 0.1 2014年3月16日 下午3:17:31 nero Exp $
 */
public class MvcSyncTokenInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG        = Logger.getLogger(MvcSyncTokenInterceptor.class);

    private static final String TOKEN_NAME = "_synToken";

    private String              isEnable;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        if (!isEnable.equals("enable")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession(true);
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        CheckToken annotation = method.getAnnotation(CheckToken.class);

        boolean isSuccess = true;
        boolean resetToken = false;

        if (annotation != null) {

            isSuccess = check(request);

            if (!isSuccess) {
                LOG.warn("Illegal request,[url:" + request.getServletPath() + "]");
            }

            resetToken = annotation.resetToken();

        }

        String token = (String) session.getAttribute(TOKEN_NAME);

        if (resetToken || token == null) {
            session.setAttribute(TOKEN_NAME, TokenProcessor.getInstance().generateToken(request));
        }

        return isSuccess;
    }

    /**
     * 检查request是否合法
     * @param request
     * @return
     */
    private boolean check(HttpServletRequest request) {

        String serverToken = (String) request.getSession(false).getAttribute(TOKEN_NAME);

        if (serverToken == null) {
            return false;
        }
        String clinetToken = request.getParameter(TOKEN_NAME);
        if (clinetToken == null) {
            return false;
        }
        if (!serverToken.equals(clinetToken)) {
            return false;
        }
        return true;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

}
