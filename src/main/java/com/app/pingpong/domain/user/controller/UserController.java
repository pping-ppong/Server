package com.app.pingpong.domain.user.controller;

import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/users")
public class UserController {

    private final UserService userService;

    @ResponseBody
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> callBack(@RequestBody SignUpRequest signUpRequest) {
        return new ResponseEntity<>(userService.signup(signUpRequest), HttpStatus.OK);
    }
}
