package com.app.pingpong.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserOauthRequest {
    private String email;
    private String socialIdx;
}
