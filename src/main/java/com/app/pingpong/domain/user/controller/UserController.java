package com.app.pingpong.domain.user.controller;

import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.request.UpdateRequestDto;
import com.app.pingpong.domain.user.dto.response.SearchHistoryResponse;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.dto.response.UserSearchResponse;
import com.app.pingpong.domain.user.service.UserService;
import com.app.pingpong.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<UserResponse> singUp(@RequestBody SignUpRequest request) {
        return new BaseResponse<>(userService.signup(request));
    }

    @ResponseBody
    @PostMapping("/validate")
    public BaseResponse<String> validate(@RequestParam String nickname) {
        return userService.validateNickname(nickname);
    }

    @PatchMapping("/{userIdx}")
    public BaseResponse<UserResponse> update(@RequestBody UpdateRequestDto request, @PathVariable("userIdx") Long userIdx) {
        return new BaseResponse<>(userService.update(request, userIdx));
    }

    @GetMapping("/search")
    public BaseResponse<List<UserSearchResponse>> search(@RequestParam("nickname") String nickname) {
        return new BaseResponse<>(userService.search(nickname));
    }

    @ResponseBody
    @GetMapping("/{userIdx}/search-history")
    public List<SearchHistoryResponse> findSearchHistory(@PathVariable("userIdx") Long userIdx) {
        return userService.findSearchHistory(userIdx);
    }

    @ResponseBody
    @GetMapping("/{userIdx}/mypage")
    public void getProfile(@RequestBody SignUpRequest request) {
        userService.findUserProfile(request);
    }

}
