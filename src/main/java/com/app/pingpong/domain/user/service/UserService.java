package com.app.pingpong.domain.user.service;

import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.UserRepository;
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
        // 1. null값 체크

        // 2. 이미 등록된 유저인지 체크

        // 3. 프로필 이미지 s3에 업로드하기

        // 4. 닉네임 형식 체크 -> 한글 영문 조합 10자 이내
        if (!isRegexNickname(request.getNickname())) {

        }

        User user = userRepository.save(
                User.builder()
                        .socialIdx(Long.parseLong(request.getSocialIdx()))
                        .email(request.getEmail())
                        .nickname(request.getNickname())
                        .profileImage(request.getProfileImage())
                        .build());

        return new UserResponse(user.getUserIdx());
    }

}
