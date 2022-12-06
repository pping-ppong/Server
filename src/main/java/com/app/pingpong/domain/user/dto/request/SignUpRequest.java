package com.app.pingpong.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String socialIdx;
    private String email;
    private String nickname;
    private String profileImage;
}
