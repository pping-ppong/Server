package com.app.pingpong.domain.group.service;

import com.app.pingpong.domain.facade.UserFacade;
import com.app.pingpong.domain.group.dto.request.GroupRequest;
import com.app.pingpong.domain.group.dto.response.GroupResponse;
import com.app.pingpong.domain.group.entity.Team;
import com.app.pingpong.domain.group.repository.TeamRepository;
import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.app.pingpong.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.app.pingpong.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserFacade userFacade;

    public GroupResponse createGroup(GroupRequest groupRequest) {
        // 그룹 이름 설정
        Team team = Team.builder()
                .name(groupRequest.getGroupName())
                .build();
        // 현재 유저를 호스트로 설정
        User currentUser = userFacade.getCurrentUser();
        currentUser.setTeam(team);
        team.setHost(currentUser);

        for (Long memberIdx : groupRequest.getMemberIdx()) {
            if (memberIdx == currentUser.getId()) {
                throw  new BaseException(INVALID_GROUP_MEMBER);
            }
            Optional<User> user = userRepository.findById(memberIdx);
            user.ifPresent(selectUser -> {
                selectUser.setTeam(team);
                }
            );
        }
        Team newTeam = teamRepository.save(team);
        return GroupResponse.of(newTeam);
    }
}
