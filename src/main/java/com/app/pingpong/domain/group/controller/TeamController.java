package com.app.pingpong.domain.group.controller;

import com.app.pingpong.domain.group.dto.request.GroupRequest;
import com.app.pingpong.domain.group.dto.response.GroupResponse;
import com.app.pingpong.domain.group.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/groups")
public class TeamController {

    private final TeamService teamService;

    @ResponseBody
    @PostMapping("")
    public GroupResponse createGroup(@RequestBody GroupRequest groupRequest) {
        return teamService.createGroup(groupRequest);
    }
}
