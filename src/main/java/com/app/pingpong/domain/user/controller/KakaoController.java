package com.app.pingpong.domain.user.controller;

import com.app.pingpong.domain.user.dto.response.KakaoResponse;
import com.app.pingpong.domain.user.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class KakaoController {

    private final KakaoService kakaoService;

    /* 카카오에서 인가 코드를 가져옴 */



    /* 카카오에서 정보를 가져옴*/
    @ResponseBody
    @GetMapping("/kakao/info")
    public ResponseEntity<KakaoResponse> callBack(@RequestParam String code) {
        String accessToken = kakaoService.getKakaoAccessToken(code);
        KakaoResponse kakaoUserInfo = kakaoService.getKakaoUserInfo(accessToken);
        return new ResponseEntity<>(kakaoUserInfo, HttpStatus.OK);
    }
}
