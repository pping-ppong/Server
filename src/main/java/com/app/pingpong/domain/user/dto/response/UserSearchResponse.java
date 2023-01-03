package com.app.pingpong.domain.user.dto.response;

import com.app.pingpong.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class UserSearchResponse {
    private Long userIdx;
    private String nickname;
    private String profileImage;

    public static List<UserSearchResponse> of(List<User> users) {
        return users.stream().map(user -> UserSearchResponse.builder()
                .userIdx(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build()).collect(Collectors.toList());
    }
}
