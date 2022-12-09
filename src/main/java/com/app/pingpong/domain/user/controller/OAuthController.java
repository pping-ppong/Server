package com.app.pingpong.domain.user.controller;

import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.response.UserLoginResponse;
import com.app.pingpong.domain.user.dto.response.UserOAuthResponse;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.service.OAuthService;
import com.app.pingpong.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oauthService;
    private final UserService userService;

    /* 카카오 로그인 */
    @ResponseBody
    @GetMapping("/kakao")
    public ResponseEntity<UserLoginResponse> kakaoLogin(@RequestParam String code) {
        String accessToken = oauthService.getKakaoAccessToken(code);
        UserLoginResponse kakaoLoginUser = oauthService.kakaoLogin(accessToken);
        return new ResponseEntity<>(kakaoLoginUser, HttpStatus.OK);
    }

    /* 구글 로그인 */
    @ResponseBody
    @GetMapping("/google")
    public ResponseEntity<UserOAuthResponse> googleLogin(@RequestParam String code) {
        String accessToken = oauthService.getGoogleAccessToken(code);
        UserOAuthResponse googleUserInfo = oauthService.getGoogleUserInfo(accessToken);
        return new ResponseEntity<>(googleUserInfo, HttpStatus.OK);
    }
}
