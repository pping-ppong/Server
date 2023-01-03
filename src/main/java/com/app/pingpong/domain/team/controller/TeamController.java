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
    public BaseResponse<TeamResponse> create(@RequestBody TeamRequest groupRequest) {
        return new BaseResponse<>(teamService.create(groupRequest));
    }

    @ResponseBody
    @GetMapping("/{teamIdx}")
    public BaseResponse<List<TeamMemberResponse>> findTeamMembers(@PathVariable("teamIdx") Long teamIdx) {
        return new BaseResponse<>(teamService.findTeamMembers(teamIdx));
    }

    @ResponseBody
    @PatchMapping("/{teamIdx}/host")
    public BaseResponse<TeamResponse> updateHost(@PathVariable("teamIdx") Long teamIdx, @RequestParam Long delegatorIdx) {
        return new BaseResponse<>(teamService.updateHost(teamIdx, delegatorIdx));
    }
}
