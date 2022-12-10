package com.app.pingpong.global.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtils {
    public static String getLoginUsername(){
        Authentication loginUser = SecurityContextHolder.getContext().getAuthentication();
        return loginUser.getName();
    }
}
