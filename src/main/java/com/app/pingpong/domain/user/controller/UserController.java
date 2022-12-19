package com.app.pingpong.domain.user.controller;

import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.response.SearchHistoryResponse;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.dto.response.UserSearchResponse;
import com.app.pingpong.domain.user.service.UserService;
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

    /* 회원가입 */
    @ResponseBody
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> singUp(@RequestBody SignUpRequest signUpRequest) {
        return new ResponseEntity<>(userService.signup(signUpRequest), HttpStatus.OK);
    }

    /* 회원가입 시 닉네임 유효성 검사 */
    @ResponseBody
    @PostMapping("/validate")
    public void validateNickname(@RequestParam String nickname) {
        userService.validateNickname(nickname);
    }

    /* 자신의 조회 - 마이페이지 */
    @ResponseBody
    @GetMapping("/{userIdx}/mypage")
    public void findUserProfile(@RequestBody SignUpRequest signUpRequest) {
        userService.findUserProfile(signUpRequest);
    }

    /* 유저 검색 by 닉네임*/
    @ResponseBody
    @GetMapping("/search")
    public void search(@RequestParam("nickname") String nickname) {
        userService.search(nickname);
    }

    /* 해당 유저의 검색 기록 조회 */
    @ResponseBody
    @GetMapping("/{userIdx}/search-history")
    public List<SearchHistoryResponse> findSearchHistory(@PathVariable("userIdx") Long userIdx) {
        return userService.findSearchHistory(userIdx);
    }

}
