package com.app.pingpong.domain.user.dto.response;

import com.app.pingpong.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long userIdx;

    public static UserResponse of(User user) {
        return new UserResponse(user.getId());
    }
}
