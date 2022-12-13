package com.app.pingpong.domain.user.dto.response;

import com.app.pingpong.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserSearchRes {
    private Long userIdx;
    private String nickname;
    private String profileImage;

    public UserSearchRes(User user) {
        this.userIdx = user.getUserIdx();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}
