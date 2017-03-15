package com.sleepwalker.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserAuthCredentials extends UserDetails {
    public int getId();
}