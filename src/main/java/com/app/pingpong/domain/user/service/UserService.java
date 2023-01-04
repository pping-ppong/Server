package com.app.pingpong.domain.user.service;

import com.app.pingpong.domain.facade.UserFacade;
import com.app.pingpong.domain.team.dto.response.TeamDetailResponse;
import com.app.pingpong.domain.team.dto.response.TeamMemberResponse;
import com.app.pingpong.domain.team.entity.Team;
import com.app.pingpong.domain.team.entity.UserTeam;
import com.app.pingpong.domain.team.repository.TeamRepository;
import com.app.pingpong.domain.team.repository.UserTeamRepository;
import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.request.UpdateRequestDto;
import com.app.pingpong.domain.user.dto.response.SearchHistoryResponse;
import com.app.pingpong.domain.user.dto.response.UserDetailResponse;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.dto.response.UserSearchResponse;
import com.app.pingpong.domain.user.entity.SearchHistory;
import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.SearchHistoryRepository;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.app.pingpong.global.common.BaseResponse;
import com.app.pingpong.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.app.pingpong.global.exception.ErrorCode.*;
import static com.app.pingpong.global.utils.ValidationRegex.isRegexNickname;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFacade userFacade;

    @Transactional
    public UserResponse signup(SignUpRequest request) {
        User user = request.toEntity(passwordEncoder);
        return UserResponse.of(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(UpdateRequestDto request, Long userIdx) {
        /* s3에 이미지 업로드 필요*/
        User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        validateNickname(request.getNickname());
        user.setNickname(request.getNickname());
        user.setProfileImage(request.getProfileImage());
        return UserResponse.of(user);
    }

    @Transactional
    public BaseResponse<String> validateNickname(String nickname){
        if (!isRegexNickname(nickname)) {
            throw new BaseException(INVALID_NICKNAME);
        } else if (userRepository.existsUserByNickname(nickname)) {
            throw new BaseException(USER_NICKNAME_ALREADY_EXISTS);
        }
        return new BaseResponse(SUCCESS_VALIDATE_NICKNAME);
    }

    public List<UserSearchResponse> search(String nickname) {
        List<User> findUsers = userRepository.findByNicknameContains(nickname).orElseThrow(() -> new BaseException(SEARCH_USER_NICKNAME_NOT_EXISTS));
        List<UserSearchResponse> response = UserSearchResponse.of(findUsers);
        searchHistoryRepository.save(SearchHistory.builder()
                    .content(nickname)
                    .user(userFacade.getCurrentUser())
                    .build()
        );
        return response;
    }

    public List<SearchHistoryResponse> findSearchHistory(Long userId) {
        List<SearchHistory> history = searchHistoryRepository.findTop10ByUserIdOrderByIdDesc(userId);
        List<SearchHistoryResponse> findSearchHistory = new ArrayList<>();
        for (SearchHistory h : history) {
                findSearchHistory.add(new SearchHistoryResponse(h.getId(), h.getContent()));
        }
        return findSearchHistory;
    }

    public List<TeamDetailResponse> findTeamByUserId(Long userId) {
        List<UserTeam> userTeams = userTeamRepository.findAllByUserId(userId);
        List<Team> teams = userTeams.stream().map(UserTeam::getTeam).collect(Collectors.toList());
        List<Long> teamId = teams.stream().map(Team::getId).collect(Collectors.toList());

        for (Long id : teamId) {
            Team team = teamRepository.findById(id).orElseThrow();
            team.getMembers().stream().map(UserTeam::getUser).collect(Collectors.toList());
        }

        return TeamDetailResponse.of(userTeams);
    }
}
