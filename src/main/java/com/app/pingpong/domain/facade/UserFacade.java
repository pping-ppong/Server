package com.app.pingpong.domain.facade;

import com.app.pingpong.domain.user.entity.User;
import com.app.pingpong.domain.user.repository.UserRepository;
import com.app.pingpong.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static com.app.pingpong.global.exception.ErrorCode.INVALID_USER;

@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(email.equals("anonymousUser"))
            throw new BaseException(INVALID_USER);
        return userRepository.findByEmail(email).orElseThrow(() -> new BaseException(INVALID_USER));
    }
}
