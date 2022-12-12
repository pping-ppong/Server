package com.app.pingpong.domain.user.service;

import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.app.pingpong.global.common.BaseResponse;
import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.user.EmailAlreadyExistsException;
import com.app.pingpong.global.exception.user.InvalidNickNameException;
import com.app.pingpong.global.exception.user.NicknameAlreadyExistsException;
import com.sun.net.httpserver.Authenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.app.pingpong.global.utils.ValidationRegex.isRegexNickname;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse signup(SignUpRequest request) {
        validateUserInfo(request);
        User user = userRepository.findBySocialIdx(request.getSocialIdx());
        user.setNickname(request.getNickname());
        user.setProfileImage(request.getProfileImage());
        return new UserResponse(user.getUserIdx());
    }

    private void validateUserInfo(SignUpRequest request)  {
        if (!isRegexNickname(request.getNickname())) {
            throw new InvalidNickNameException();
        }
    }

    public void validateNickname(String nickname) {
        if (!isRegexNickname(nickname)) {
            throw new InvalidNickNameException();
        }
        if (userRepository.existsUserByNickname(nickname)) {
            throw new NicknameAlreadyExistsException();
        }
    }
}
