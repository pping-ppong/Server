package com.app.pingpong.domain.user.service;

import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.response.SearchHistoryResponse;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.dto.response.UserSearchRes;
import com.app.pingpong.domain.user.entity.SearchHistory;
import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.SearchHistoryRepository;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.app.pingpong.global.common.BaseResponse;
import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.BaseResultCode;
import com.app.pingpong.global.exception.user.InvalidNickNameException;
import com.app.pingpong.global.exception.user.NicknameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.app.pingpong.global.exception.BaseResultCode.DATABASE_ERROR;
import static com.app.pingpong.global.exception.BaseResultCode.SEARCH_USER_NICKNAME_NOT_EXISTS;
import static com.app.pingpong.global.utils.ValidationRegex.isRegexNickname;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    @Transactional
    public UserResponse signup(SignUpRequest request) throws InvalidNickNameException {
        validateUserInfo(request);
        User user = userRepository.findBySocialIdx(request.getSocialIdx());
        user.setNickname(request.getNickname());
        user.setProfileImage(request.getProfileImage());
        return new UserResponse(user.getId());
    }

    private void validateUserInfo(SignUpRequest request) {
        if (!isRegexNickname(request.getNickname())) {
            throw new InvalidNickNameException();
        }
    }

    public BaseResponse validateNickname(String nickname) {
        if (!isRegexNickname(nickname)) {
            throw new InvalidNickNameException();
        }
        if (userRepository.existsUserByNickname(nickname)) {
            throw new NicknameAlreadyExistsException();
        }
        return new BaseResponse<>(true, BaseResultCode.SUCCESS_VALIDATE_NICKNAME.getMessage(), BaseResultCode.SUCCESS_VALIDATE_NICKNAME.getCode());
    }

    public BaseResponse search(String nickname) {
        User user = userRepository.findByNicknameContains(nickname);
        if (user == null) {
            throw new BaseException(SEARCH_USER_NICKNAME_NOT_EXISTS);
        }

        SearchHistory history = SearchHistory.builder()
                .content(nickname)
                .user(user)
                .build();
        searchHistoryRepository.save(history);
        return new BaseResponse<>(true, BaseResultCode.SUCCESS.getMessage(), BaseResultCode.SUCCESS.getCode(), user);
    }

    public BaseResponse findSearchHistory(Long userIdx) {
        List<SearchHistory> history = searchHistoryRepository.findTop10ByUserIdOrderByIdDesc(userIdx);
        List<SearchHistoryResponse> findSearchHistory = new ArrayList<>();
        for (SearchHistory h : history) {
                findSearchHistory.add(new SearchHistoryResponse(h.getId(), h.getContent()));
        }
        return new BaseResponse<>(true, BaseResultCode.SUCCESS.getMessage(), BaseResultCode.SUCCESS.getCode(), findSearchHistory);
    }

    public BaseResponse findUserProfile(SignUpRequest signUpRequest) {
        return new BaseResponse<>(true, BaseResultCode.SUCCESS.getMessage(), BaseResultCode.SUCCESS.getCode());
    }
}
