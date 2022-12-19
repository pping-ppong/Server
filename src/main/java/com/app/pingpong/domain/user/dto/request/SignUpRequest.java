package com.app.pingpong.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private Long socialIdx;
    private String nickname;
    private String profileImage;
}
