package com.app.pingpong.domain.team.dto.response;

import com.app.pingpong.domain.user.dto.response.UserSearchResponse;
import com.app.pingpong.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
public class TeamMemberResponse {
    private Long userId;
    private String nickname;
    private String profileImage;
    private Long hostId;

    public static List<TeamMemberResponse> of(List<User> users, Long hostId) {
        return users.stream().map(user -> TeamMemberResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .hostId(hostId)
                .build()).collect(Collectors.toList());
    }
}
