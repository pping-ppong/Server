package com.app.pingpong.domain.user.dto.request.oauth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoRequest {
    private String socialType;
    private String code;
}
