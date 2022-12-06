package com.app.pingpong.domain.user.dto;

import com.app.pingpong.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuthResponse {
    private String message;
    private static int userIdx;
    private static String email;

    public static OAuthResponse from(User user) {
        return OAuthResponse.builder().build();
    }
}
