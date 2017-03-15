package com.sleepwalker.security;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;
import org.springframework.stereotype.Service;

@Service("userDetailService")
public class UserDetailService implements UserDetailsService {

    @Resource
    private UserAuthCheck         userAuthCheck;

    @Resource
    private EhCacheBasedUserCache ehCacheBasedUserCache;
    protected final Log           logger = LogFactory.getLog(getClass());

    @Override
    public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
        UserAuthCredentials userAuthCredentials = this.userAuthCheck.selectByUserName(arg0);

        if (userAuthCredentials != null)
            this.ehCacheBasedUserCache.putUserInCache(userAuthCredentials);
        else {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return userAuthCredentials;
    }

    protected Authentication createNewAuthentication(Authentication currentAuth,
                                                     String newPassword) {
        UserDetails user = loadUserByUsername(currentAuth.getName());

        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
            user, user.getPassword(), user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());

        return newAuthentication;
    }

    public void reloadAuthentication() throws AuthenticationException {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException(
                "Can't change password as no Authentication object found in context for current user.");
        }

        UserDetails user = loadUserByUsername(currentUser.getName());

        setAuthentication(user);
    }

    public void setAuthentication(UserDetails user) throws AuthenticationException {
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
            user, user.getPassword(), user.getAuthorities());
        newAuthentication.setDetails(user);

        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }
}