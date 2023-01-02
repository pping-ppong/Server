package com.app.pingpong.domain.user.controller;

import com.app.pingpong.domain.user.dto.request.oauth.TokenRequest;
import com.app.pingpong.domain.user.dto.request.oauth.UserInfoRequest;
import com.app.pingpong.domain.user.dto.request.oauth.UserOauthRequest;
import com.app.pingpong.domain.user.dto.response.TokenResponse;
import com.app.pingpong.domain.user.dto.response.UserLoginResponse;
import com.app.pingpong.domain.user.dto.response.UserOAuthResponse;
import com.app.pingpong.domain.user.service.OAuthService;
import com.app.pingpong.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {

    private final OAuthService oauthService;

    @ResponseBody
    @GetMapping("/info")
    public BaseResponse<UserOAuthResponse> getUserInfo(@RequestBody UserInfoRequest request) {
        return new BaseResponse<>(oauthService.getUserInfo(request));
    }

    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<UserLoginResponse> login(@RequestBody UserOauthRequest request) {
       return new BaseResponse<>(oauthService.login(request));
    }

    @ResponseBody
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody TokenRequest request) {
        return new ResponseEntity<>(oauthService.reissue(request), HttpStatus.OK);
    }
}
