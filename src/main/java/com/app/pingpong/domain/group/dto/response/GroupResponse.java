package com.app.pingpong.domain.group.dto.response;

import com.app.pingpong.domain.group.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GroupResponse {
    private Long groupIdx;
    private String groupName;
    private List<Long> groupMemberIdx;

    public static GroupResponse of(Team team) {
        List<Long> groupUserIdx = team.getTeamUserIdx(team);
        return new GroupResponse(team.getId(), team.getName(), groupUserIdx);
    }
}
