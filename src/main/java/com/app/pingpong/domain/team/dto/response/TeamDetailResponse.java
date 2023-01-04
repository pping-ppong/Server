package com.app.pingpong.domain.team.dto.response;

import com.app.pingpong.domain.team.entity.UserTeam;

import com.app.pingpong.domain.user.dto.response.UserDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TeamDetailResponse {
    private Long teamId;
    private String teamName;
    private List<UserDetailResponse> users;

    public static List<TeamDetailResponse> of(List<UserTeam> userTeams) {
        return userTeams.stream().map(t ->
                        new TeamDetailResponse(
                                t.getTeam().getId(),
                                t.getTeam().getName(),
                                t.getTeam().getMembers().stream().map(u ->
                                        new UserDetailResponse(u.getUser().getId(),
                                                u.getUser().getNickname(),
                                                u.getUser().getProfileImage())).collect(Collectors.toList())))
                                .collect(Collectors.toList());
    }
}
