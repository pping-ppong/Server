package com.app.pingpong.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String message;

    public static OAuthResponse from(String message) {
        return OAuthResponse.builder()
                .message(message)
                .build();
    }

}
