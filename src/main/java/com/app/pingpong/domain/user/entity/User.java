package com.app.pingpong.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialIdx;

    private String email;

    private String nickname;

    private String profileImage;

    @Enumerated
    private Authority authority;

    @Builder
    public User(String socialIdx, String email, String nickname, String profileImage, Authority authority) {
        this.socialIdx = socialIdx;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.authority = authority;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
