package com.sleepwalker.core.spring.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sleepwalker.core.spring.context.SystemContext;

public class SystemFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub   
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
                                                                                     throws IOException,
                                                                                     ServletException {
        SystemContext.setRequest((HttpServletRequest) arg0);
        SystemContext.setResponse((HttpServletResponse) arg1);

        arg2.doFilter(arg0, arg1);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub   
    }
}