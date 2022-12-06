package com.app.pingpong.domain.user.controller;

import com.app.pingpong.domain.user.dto.request.KakaoLoginRequest;
import com.app.pingpong.domain.user.dto.response.LoginResponse;
import com.app.pingpong.domain.user.service.KakaoService;
import com.app.pingpong.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/users")
public class UserController {

    private final UserService userService;

}
