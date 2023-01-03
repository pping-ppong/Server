package com.app.pingpong.domain.team.service;

import com.app.pingpong.domain.facade.UserFacade;
import com.app.pingpong.domain.team.dto.request.TeamRequest;
import com.app.pingpong.domain.team.dto.response.TeamResponse;
import com.app.pingpong.domain.team.entity.Team;
import com.app.pingpong.domain.team.entity.UserTeam;
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

    public TeamResponse create(TeamRequest groupRequest) {
        if (groupRequest.getMemberIdx().size() > 10 || groupRequest.getMemberIdx().size() < 1) {
            throw new BaseException(INVALID_TEAM_MEMBER_SIZE);
        }

        User currentUser = userFacade.getCurrentUser();
        if (teamRepository.findByHostId(currentUser.getId()).size() > 6) {
            throw new BaseException(EXCEED_HOST_TEAM_SIZE);
        }

        Team team = Team.builder()
                .name(groupRequest.getGroupName())
                .build();
        Team newTeam = teamRepository.save(team);

        UserTeam hostUserTeam = new UserTeam();
        hostUserTeam.setTeam(newTeam);
        hostUserTeam.setUser(currentUser);
        userTeamRepository.save(hostUserTeam);

        for (Long memberIdx : groupRequest.getMemberIdx()) {
            UserTeam userTeam = new UserTeam();
            userTeam.setTeam(newTeam);
            userTeam.getTeam().setHost(currentUser);
            if (memberIdx == currentUser.getId()) {
                throw  new BaseException(INVALID_GROUP_MEMBER);
            }
            User user = userRepository.findById(memberIdx).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
            userTeam.setUser(user);
            userTeamRepository.save(userTeam);
        }
        List<UserTeam> userTeams = userTeamRepository.findAllByTeamId(newTeam.getId());
        return TeamResponse.of(userTeams);
    }

    public TeamResponse updateHost(Long teamIdx, Long delegatorIdx) {
        Team team = teamRepository.findById(teamIdx).orElseThrow(() -> new BaseException(TEAM_NOT_FOUND));
        List<User> teamUsers = team.getMembers().stream().map(UserTeam::getUser).collect(Collectors.toList());
        User delegator = userRepository.findById(delegatorIdx).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        if (!teamUsers.contains(delegator)) {
            throw new BaseException(DELEGATOR_NOT_FOUND);
        }
        team.setHost(delegator);
        teamRepository.save(team);
        return TeamResponse.of(team);
    }
}
