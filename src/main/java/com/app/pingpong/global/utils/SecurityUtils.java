package com.app.pingpong.global.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtils {
    public static Long getCurrentUserIdx(){
        Authentication loginUser = SecurityContextHolder.getContext().getAuthentication();
        if (loginUser == null || loginUser.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }
        return Long.parseLong(loginUser.getName());
    }
}
