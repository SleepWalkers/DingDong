package com.sleepwalker.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.UserException;

public interface SecurityService {

    public UserAuthCredentials getCurrentUser();

    public boolean isLogin();

    public void login(HttpServletRequest request, HttpServletResponse response, String username,
                      String password) throws UserException;

    public void logout(HttpServletRequest request, HttpServletResponse response);

    public void setUserDetails(HttpServletRequest request, UserAuthCredentials userAuthCredentials);

    public void reloadUserDetails();

    public void removeCache(int paramInt);

    public void removeCache(String paramString);

    public void removeCache(UserAuthCredentials userAuthCredentials);

    public String getDefaultCache();

}
