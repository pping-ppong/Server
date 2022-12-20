package com.app.pingpong.domain.user.dto.response;

import com.app.pingpong.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private Long userIdx;
    private String accessToken;
    private String refreshToken;

    public static UserLoginResponse of(User user, TokenResponse token) {
        return new UserLoginResponse(user.getId(), token.getAccessToken(), token.getRefreshToken());
    }
}
