package com.app.pingpong.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSearchResponse {
    private Long userIdx;
    private String nickname;
    private String profileImage;


}
