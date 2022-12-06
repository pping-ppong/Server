package com.app.pingpong.domain.user.service;

import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.user.EmailAlreadyExistsException;
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
        /*
        * 1. 유효성 체크
        * 2. S3에 사진 업로드
        * */

        User user = userRepository.save(
                User.builder()
                        .socialIdx(Long.parseLong(request.getSocialIdx()))
                        .email(request.getEmail())
                        .nickname(request.getNickname())
                        .profileImage(request.getProfileImage())
                        .build());

        return new UserResponse(user.getUserIdx());
    }

    private void validateUserInfo(SignUpRequest request)  {
        /*
        * 1. null값 체크
        * 2. 이미 등록된 유저인지 체크
        * 3. 닉네임 유효성 체크
        * */

        boolean existsEmail = userRepository.existsUserByEmail(request.getEmail());
        if (existsEmail) {
            throw new EmailAlreadyExistsException();
        }

    }

}
