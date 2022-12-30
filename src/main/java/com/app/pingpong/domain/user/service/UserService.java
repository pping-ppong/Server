package com.app.pingpong.domain.user.service;

import com.app.pingpong.domain.facade.UserFacade;
import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.response.SearchHistoryResponse;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.dto.response.UserSearchResponse;
import com.app.pingpong.domain.user.entity.SearchHistory;
import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.SearchHistoryRepository;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.app.pingpong.global.common.BaseResponse;
import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.BaseRowSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.app.pingpong.global.exception.ErrorCode.*;
import static com.app.pingpong.global.utils.ValidationRegex.isRegexNickname;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFacade userFacade;
    private final SearchHistoryRepository searchHistoryRepository;

    @Transactional
    public UserResponse signup(SignUpRequest signUpRequest) {
        User user = signUpRequest.toEntity(passwordEncoder);
        return UserResponse.of(userRepository.save(user));
    }

    @Transactional
    public BaseResponse<String> validateNickname(String nickname) {
        if (!isRegexNickname(nickname)) {
            throw new BaseException(INVALID_NICKNAME);
        }
        if (userRepository.existsUserByNickname(nickname)) {
            throw new BaseException(USER_NICKNAME_ALREADY_EXISTS);
        }
        return new BaseResponse(SUCCESS_VALIDATE_NICKNAME);
    }

    public List<UserSearchResponse> search(String nickname) {
        List<User> findUser = userRepository.findByNicknameContains(nickname)
                .orElseThrow(() -> new BaseException(SEARCH_USER_NICKNAME_NOT_EXISTS));

        List<UserSearchResponse> response = findUser.stream().map(user -> UserSearchResponse.builder()
                .userIdx(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build()
        ).collect(Collectors.toList());

        // 닉네임 내역 저장
        User currentUser = userFacade.getCurrentUser();
        SearchHistory history = SearchHistory.builder()
                .content(nickname)
                .user(currentUser)
                .build();
        searchHistoryRepository.save(history);
        return response;
    }

    public List<SearchHistoryResponse> findSearchHistory(Long userIdx) {
        List<SearchHistory> history = searchHistoryRepository.findTop10ByUserIdOrderByIdDesc(userIdx);
        List<SearchHistoryResponse> findSearchHistory = new ArrayList<>();
        for (SearchHistory h : history) {
                findSearchHistory.add(new SearchHistoryResponse(h.getId(), h.getContent()));
        }
        return findSearchHistory;
    }

    public void findUserProfile(SignUpRequest signUpRequest) {
        //return new BaseResponse<>(true, BaseResultCode.SUCCESS.getMessage(), BaseResultCode.SUCCESS.getCode());
    }
}
