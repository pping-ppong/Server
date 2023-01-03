package com.app.pingpong.domain.team.dto.response;

import com.app.pingpong.domain.team.entity.Team;
import com.app.pingpong.domain.team.entity.UserTeam;
import com.app.pingpong.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TeamResponse {
    private Long teamId;
    private String teamName;
    private List<Long> teamMemberIdx;

    public static TeamResponse of(Team team) {
        List<User> users = team.getMembers().stream().map(UserTeam::getUser).collect(Collectors.toList());
        List<Long> usersIdx = users.stream().map(User::getId).toList();
        return new TeamResponse(team.getId(), team.getName(), usersIdx);
    }

    public static TeamResponse of(List<UserTeam> team) {
        List<User> users = team.stream().map(UserTeam::getUser).collect(Collectors.toList());
        List<Long> usersIdx = users.stream().map(User::getId).toList();
        return new TeamResponse(team.get(0).getTeam().getId(), team.get(0).getTeam().getName(), usersIdx);
    }
}
