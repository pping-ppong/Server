package com.app.pingpong.domain.team.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequest {
    private String groupName;
    private List<Long> memberId;
}
