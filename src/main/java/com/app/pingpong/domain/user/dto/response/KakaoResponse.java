package com.app.pingpong.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoResponse {
    private String socialIdx;
    private String email;
}
