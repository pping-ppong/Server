package com.app.pingpong.domain.user.controller;

import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.response.SearchHistoryResponse;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.dto.response.UserSearchResponse;
import com.app.pingpong.domain.user.service.UserService;
import com.app.pingpong.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/users")
public class UserController {

    private final UserService userService;

    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<UserResponse> singUp(@RequestBody SignUpRequest signUpRequest) {
        return new BaseResponse<>(userService.signup(signUpRequest));
    }

    @ResponseBody
    @PostMapping("/validate")
    public BaseResponse<String> validateNickname(@RequestParam String nickname) {
        return userService.validateNickname(nickname);
    }

    @GetMapping("/search")
    public BaseResponse<List<UserSearchResponse>> search(@RequestParam("nickname") String nickname) {
        return new BaseResponse<>(userService.search(nickname));
    }

    /* 해당 유저의 검색 기록 조회 */
    @ResponseBody
    @GetMapping("/{userIdx}/search-history")
    public List<SearchHistoryResponse> findSearchHistory(@PathVariable("userIdx") Long userIdx) {
        return userService.findSearchHistory(userIdx);
    }

    /* 자신의 조회 - 마이페이지 */
    @ResponseBody
    @GetMapping("/{userIdx}/mypage")
    public void findUserProfile(@RequestBody SignUpRequest signUpRequest) {
        userService.findUserProfile(signUpRequest);
    }

}
