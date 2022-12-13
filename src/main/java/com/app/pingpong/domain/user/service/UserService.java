package com.app.pingpong.domain.user.service;

import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.dto.response.UserSearchRes;
import com.app.pingpong.domain.user.entity.SearchHistory;
import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.SearchHistoryRepository;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.app.pingpong.global.common.BaseResponse;
import com.app.pingpong.global.exception.BaseResultCode;
import com.app.pingpong.global.exception.user.InvalidNickNameException;
import com.app.pingpong.global.exception.user.NicknameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.app.pingpong.global.utils.ValidationRegex.isRegexNickname;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    @Transactional
    public UserResponse signup(SignUpRequest request) {
        validateUserInfo(request);
        User user = userRepository.findBySocialIdx(request.getSocialIdx());
        user.setNickname(request.getNickname());
        user.setProfileImage(request.getProfileImage());
        return new UserResponse(user.getId());
    }

    private void validateUserInfo(SignUpRequest request)  {
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
        return new BaseResponse<>(BaseResultCode.SUCCESS_VALIDATE_NICKNAME);
    }

    public UserSearchRes search(String nickname) {
        // 1. 특정 닉네임 문자열을 포함하는 유저 검색 10명만
        User user = userRepository.findByNicknameContains(nickname);

        // 2. 검색 기록 저장
        SearchHistory history = SearchHistory.builder()
                .content(nickname)
                .user(user)
                .build();
        searchHistoryRepository.save(history);

       return new UserSearchRes(user);
    }
}
