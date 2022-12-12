package com.app.pingpong.domain.user.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}
