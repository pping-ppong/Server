package com.app.pingpong.domain.user.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestDto {
    private String nickname;
    private String profileImage;
}
