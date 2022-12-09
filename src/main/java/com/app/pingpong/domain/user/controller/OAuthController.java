package com.app.pingpong.domain.user.controller;

import com.app.pingpong.domain.user.dto.response.GoogleResponse;
import com.app.pingpong.domain.user.dto.response.KakaoResponse;
import com.app.pingpong.domain.user.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService OAuthService;

    /* 카카오에서 정보를 가져옴*/
    @ResponseBody
    @GetMapping("/kakao/info")
    public ResponseEntity<KakaoResponse> getKakaoUserInfo(@RequestParam String code) {
        String accessToken = OAuthService.getKakaoAccessToken(code);
        KakaoResponse kakaoUserInfo = OAuthService.getKakaoUserInfo(accessToken);
        return new ResponseEntity<>(kakaoUserInfo, HttpStatus.OK);
    }

    /* 구글에서 정보를 가져옴*/
    @ResponseBody
    @GetMapping("/google/info")
    public ResponseEntity<GoogleResponse> getGoogleUserInfo(@RequestParam String code) {
        String accessToken = OAuthService.getGoogleAccessToken(code);
        GoogleResponse googleUserInfo = OAuthService.getGoogleUserInfo(accessToken);
        return new ResponseEntity<>(googleUserInfo, HttpStatus.OK);
    }
}
