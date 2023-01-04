package com.app.pingpong.domain.user.dto.response;

import com.app.pingpong.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class UserDetailResponse {
    private Long userId;
    private String nickname;
    private String profileImage;

    public static List<UserDetailResponse> of(List<User> users) {
        return users.stream().map(user -> UserDetailResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build()).collect(Collectors.toList());
    }
}
