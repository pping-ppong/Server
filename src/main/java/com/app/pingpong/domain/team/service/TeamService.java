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

import static com.app.pingpong.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserFacade userFacade;

    public TeamResponse createGroup(TeamRequest groupRequest) {
        Team team = Team.builder()
                .name(groupRequest.getGroupName())
                .build();
        Team newTeam = teamRepository.save(team);

        User currentUser = userFacade.getCurrentUser();
        for (Long memberIdx : groupRequest.getMemberIdx()) {
            UserTeam userTeam = new UserTeam();
            userTeam.setTeam(newTeam);
            userTeam.getTeam().setHost(currentUser);
            if (memberIdx == currentUser.getId()) {
                throw  new BaseException(INVALID_GROUP_MEMBER);
            }
            User user = userRepository.findById(memberIdx).orElseThrow();
            userTeam.setUser(user);
            userTeamRepository.save(userTeam);
        }
        List<UserTeam> userTeams = userTeamRepository.findAllByTeamId(newTeam.getId());
        return TeamResponse.of(userTeams);
    }
}
