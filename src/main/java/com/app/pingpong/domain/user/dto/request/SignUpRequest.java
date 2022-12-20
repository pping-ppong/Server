package com.app.pingpong.domain.user.dto.request;

import com.app.pingpong.domain.user.entity.Authority;
import com.app.pingpong.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String socialIdx;
    private String email;
    private String nickname;
    private String profileImage;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .socialIdx(passwordEncoder.encode(socialIdx))
                .email(email)
                .nickname(nickname)
                .profileImage(profileImage)
                .authority(Authority.ROLE_USER)
                .build();
    }
}
