package com.app.pingpong.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private Long userIdx;
    private Long socialIdx;
    private String email;
    private String nickname;
    private String profileImage;
    private String accessToken;
    private String refreshToken;
}
