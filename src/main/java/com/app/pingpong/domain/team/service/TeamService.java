package com.app.pingpong.domain.team.service;

import com.app.pingpong.domain.facade.UserFacade;
import com.app.pingpong.domain.team.dto.request.TeamRequest;
import com.app.pingpong.domain.team.dto.response.TeamMemberResponse;
import com.app.pingpong.domain.team.dto.response.TeamResponse;
import com.app.pingpong.domain.team.entity.Team;
import com.app.pingpong.domain.team.entity.TeamMember;
import com.app.pingpong.domain.team.repository.TeamRepository;
import com.app.pingpong.domain.team.repository.UserTeamRepository;
import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.app.pingpong.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.app.pingpong.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserFacade userFacade;

    public TeamResponse create(TeamRequest request) {
        if (request.getMemberId().size() > 10 || request.getMemberId().size() < 1) {
            throw new BaseException(INVALID_TEAM_MEMBER_SIZE);
        }

        User currentUser = userFacade.getCurrentUser();
        if (teamRepository.findByHostId(currentUser.getId()).size() > 6) {
            throw new BaseException(EXCEED_HOST_TEAM_SIZE);
        }

        Team team = Team.builder()
                .name(request.getTeamName())
                .build();
        Team newTeam = teamRepository.save(team);

        TeamMember hostTeamMember = new TeamMember();
        hostTeamMember.setTeam(newTeam);
        hostTeamMember.setUser(currentUser);
        userTeamRepository.save(hostTeamMember);

        for (Long memberId : request.getMemberId()) {
            TeamMember teamMember = new TeamMember();
            teamMember.setTeam(newTeam);
            teamMember.getTeam().setHost(currentUser);
            if (memberId == currentUser.getId()) {
                throw  new BaseException(INVALID_GROUP_MEMBER);
            }
            User user = userRepository.findById(memberId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
            teamMember.setUser(user);
            userTeamRepository.save(teamMember);
        }
        List<TeamMember> teamMembers = userTeamRepository.findAllByTeamId(newTeam.getId());
        return TeamResponse.of(teamMembers);
    }

    public TeamResponse updateHost(Long teamId, Long delegatorId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new BaseException(TEAM_NOT_FOUND));
        List<User> teamUsers = team.getMembers().stream().map(TeamMember::getUser).collect(Collectors.toList());
        User delegator = userRepository.findById(delegatorId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        if (!teamUsers.contains(delegator)) {
            throw new BaseException(DELEGATOR_NOT_FOUND);
        }
        team.setHost(delegator);
        teamRepository.save(team);
        return TeamResponse.of(team);
    }

    public List<TeamMemberResponse> findTeamMembers(Long teamId) {
        List<TeamMember> teamMembers = userTeamRepository.findAllByTeamId(teamId);
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new BaseException(TEAM_NOT_FOUND));
        Long hostId = team.getHost().getId();
        List<User> users = teamMembers.stream().map(TeamMember::getUser).collect(Collectors.toList());
        return TeamMemberResponse.of(users, hostId);
    }
}
