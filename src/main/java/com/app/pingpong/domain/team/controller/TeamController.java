package com.app.pingpong.domain.team.controller;

import com.app.pingpong.domain.team.dto.request.TeamRequest;
import com.app.pingpong.domain.team.dto.response.TeamMemberResponse;
import com.app.pingpong.domain.team.dto.response.TeamResponse;
import com.app.pingpong.domain.team.service.TeamService;
import com.app.pingpong.domain.user.dto.response.UserSearchResponse;
import com.app.pingpong.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @ResponseBody
    @PostMapping("")
    public BaseResponse<TeamResponse> create(@RequestBody TeamRequest request) {
        return new BaseResponse<>(teamService.create(request));
    }

    @ResponseBody
    @GetMapping("/{teamId}")
    public BaseResponse<List<TeamMemberResponse>> findTeamMembers(@PathVariable("teamId") Long teamId) {
        return new BaseResponse<>(teamService.findTeamMembers(teamId));
    }

    @ResponseBody
    @PatchMapping("/{teamId}/host")
    public BaseResponse<TeamResponse> updateHost(@PathVariable("teamId") Long teamId, @RequestParam Long delegatorId) {
        return new BaseResponse<>(teamService.updateHost(teamId, delegatorId));
    }

    @ResponseBody
    @PatchMapping("/{teamId}/emit")
    public BaseResponse<List<TeamMemberResponse>> emit(@PathVariable("teamId") Long teamId, @RequestParam Long emitterId) {
        return new BaseResponse<>(teamService.emit(teamId, emitterId));
    }

}
