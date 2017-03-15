package com.sleepwalker.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

public class SampleAuthenticationManager implements AuthenticationManager {
    static final List<GrantedAuthority> AUTHORITIES = new ArrayList<>();

    static {
        AUTHORITIES.add(new UserAuthority("ROLE_USER"));
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        if (auth.getName().equals(auth.getCredentials())) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                auth.getName(), auth.getCredentials(), AUTHORITIES);
            usernamePasswordAuthenticationToken.setDetails(auth.getPrincipal());
            return usernamePasswordAuthenticationToken;
        }
        throw new BadCredentialsException("Bad Credentials");
    }
}