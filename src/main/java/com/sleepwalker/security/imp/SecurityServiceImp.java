package com.sleepwalker.security.imp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.Assert;

import com.sleepwalker.security.SecurityService;
import com.sleepwalker.security.UserAuthCheck;
import com.sleepwalker.security.UserAuthCredentials;
import com.sleepwalker.security.UserDetailService;

public class SecurityServiceImp implements SecurityService {
    private DaoAuthenticationProvider                            daoAuthenticationProvider;
    private UserDetailService                                    userDetailService;

    @Autowired(required = false)
    private UserAuthCheck                                        userAuthCheck;

    @Autowired
    private EhCacheBasedUserCache                                ehCacheBasedUserCache;
    protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    protected final Log                                          logger                      = LogFactory
                                                                                                 .getLog(getClass());

    private boolean                                              invalidateHttpSession       = true;
    private boolean                                              clearAuthentication         = true;

    private String                                               defaultCache                = cache.session
                                                                                                 .toString();

    @Override
    public UserAuthCredentials getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object userObject = authentication.getPrincipal();

            if ((userObject != null) && ((userObject instanceof UserDetails))) {
                UserAuthCredentials userAuthCredentials = (UserAuthCredentials) userObject;
                if (userAuthCredentials.getId() == 0) {
                    return null;
                }
                if (this.defaultCache.equals(cache.cache.toString())) {
                    String username = userAuthCredentials.getUsername();
                    if (username != null) {
                        UserDetails element = this.ehCacheBasedUserCache.getUserFromCache(username);
                        if (element == null) {
                            UserAuthCredentials userAuthCredentialsCache = this.userAuthCheck
                                .selectByUserName(userAuthCredentials.getUsername());
                            this.ehCacheBasedUserCache.putUserInCache(userAuthCredentialsCache);
                            return userAuthCredentialsCache;
                        }
                        return (UserAuthCredentials) element;
                    }
                } else {
                    if (this.defaultCache.equals(cache.session.toString())) {
                        return userAuthCredentials;
                    }

                    UserAuthCredentials result = this.userAuthCheck.selectById(userAuthCredentials
                        .getId());
                    return result;
                }
            }
        }

        return null;
    }

    @Override
    public void login(HttpServletRequest request, HttpServletResponse response, String username,
                      String password) throws UserException {
        request.getSession(true);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }
        try {
            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

            setDetails(request, authRequest);

            Authentication result = this.daoAuthenticationProvider.authenticate(authRequest);

            SecurityContextHolder.getContext().setAuthentication(result);
        } catch (AuthenticationException e) {
            //            if ((e instanceof UsernameNotFoundException)) {
            //                throw new UserNotFoundException();
            //            }
            //            throw new UserPasswordWrongException();
        }
    }

    protected void setDetails(HttpServletRequest request,
                              UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    @Override
    public boolean isLogin() {
        UserAuthCredentials userAuthCredentials = getCurrentUser();
        if (userAuthCredentials == null) {
            return false;
        }
        return true;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request, "HttpServletRequest required");
        if (this.invalidateHttpSession) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                this.logger.debug("Invalidating session: " + session.getId());
                session.invalidate();
            }
        }

        if (this.clearAuthentication) {
            SecurityContext context = SecurityContextHolder.getContext();
            TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices(
                "yunjee", this.userDetailService);

            tokenBasedRememberMeServices.logout(request, response, context.getAuthentication());
            context.setAuthentication(null);
        }

        SecurityContextHolder.clearContext();
    }

    public boolean isInvalidateHttpSession() {
        return this.invalidateHttpSession;
    }

    public void setInvalidateHttpSession(boolean invalidateHttpSession) {
        this.invalidateHttpSession = invalidateHttpSession;
    }

    public void setClearAuthentication(boolean clearAuthentication) {
        this.clearAuthentication = clearAuthentication;
    }

    @Override
    public void setUserDetails(HttpServletRequest request, UserAuthCredentials userAuthCredentials) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof UsernamePasswordAuthenticationToken)) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
            auth.setDetails(userAuthCredentials);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT",
            SecurityContextHolder.getContext());
    }

    @Override
    public void reloadUserDetails() {
        this.userDetailService.reloadAuthentication();
    }

    @Override
    public void removeCache(int userId) {
        UserAuthCredentials userAuthCredentials = this.userAuthCheck.selectById(userId);
        removeCache(userAuthCredentials);
    }

    @Override
    public void removeCache(String username) {
        if (this.defaultCache.equals(cache.cache.toString()))
            this.ehCacheBasedUserCache.removeUserFromCache(username);
        else if (this.defaultCache.equals(cache.session.toString()))
            reloadUserDetails();
        else
            ;
    }

    @Override
    public void removeCache(UserAuthCredentials userAuthCredentials) {
        if (this.defaultCache.equals(cache.cache.toString()))
            this.ehCacheBasedUserCache.removeUserFromCache(userAuthCredentials);
        else if (this.defaultCache.equals(cache.session.toString()))
            reloadUserDetails();
        else
            ;
    }

    @Override
    public String getDefaultCache() {
        return this.defaultCache;
    }

    public void setDefaultCache(String defaultCache) {
        this.defaultCache = defaultCache;
    }

    public DaoAuthenticationProvider getDaoAuthenticationProvider() {
        return this.daoAuthenticationProvider;
    }

    public void setDaoAuthenticationProvider(DaoAuthenticationProvider daoAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    public UserDetailService getUserDetailService() {
        return this.userDetailService;
    }

    public void setUserDetailService(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    public static enum cache {
        cache, session, none;
    }
}