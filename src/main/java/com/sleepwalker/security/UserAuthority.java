package com.sleepwalker.security;

import org.springframework.security.core.GrantedAuthority;

public class UserAuthority implements GrantedAuthority {
    private static final long serialVersionUID = 4975346429987293983L;
    private String            authority;

    public UserAuthority(String authority) {
        this.authority = authority;
    }

    public UserAuthority() {
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}