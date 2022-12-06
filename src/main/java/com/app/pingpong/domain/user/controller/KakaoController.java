package com.app.pingpong.domain.user.controller;

import com.app.pingpong.domain.user.dto.OAuthResponse;
import com.app.pingpong.domain.user.service.KakaoService;
import com.app.pingpong.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class KakaoController {

    private final KakaoService kakaoService;

    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<OAuthResponse> getKakaoAccessToken(@RequestParam String code) {
        String accessToken = kakaoService.getKakaoAccessToken(code);
        return BaseResponse.ok(
                OAuthResponse.builder()
               .message(accessToken)
                .build());
    }
}
