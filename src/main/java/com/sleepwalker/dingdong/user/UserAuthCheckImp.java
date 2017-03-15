package com.sleepwalker.dingdong.user;

import org.springframework.stereotype.Service;

import com.sleepwalker.security.UserAuthCheck;
import com.sleepwalker.security.UserAuthCredentials;

@Service("userAuthCheck")
public class UserAuthCheckImp implements UserAuthCheck {

    //    @Resource
    //    private UserService userService;

    @Override
    public UserAuthCredentials selectById(int userId) {
        return null;
        //return userService.getUser(userId);
    }

    @Override
    public UserAuthCredentials selectByUserName(String username) {
        return null;
        //  return userService.getUser(username);
    }

}
