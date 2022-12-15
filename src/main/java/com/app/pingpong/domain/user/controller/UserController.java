package com.app.pingpong.domain.user.controller;

import com.app.pingpong.domain.user.dto.request.SignUpRequest;
import com.app.pingpong.domain.user.dto.response.SearchHistoryResponse;
import com.app.pingpong.domain.user.dto.response.UserResponse;
import com.app.pingpong.domain.user.dto.response.UserSearchRes;
import com.app.pingpong.domain.user.entity.SearchHistory;
import com.app.pingpong.domain.user.service.UserService;
import com.app.pingpong.global.common.BaseResponse;
import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.BaseResultCode;
import com.app.pingpong.global.exception.user.InvalidNickNameException;
import com.app.pingpong.global.exception.user.NicknameAlreadyExistsException;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    public ResponseEntity<UserResponse> singUp(@RequestBody SignUpRequest signUpRequest) throws InvalidNickNameException {
        return new ResponseEntity<>(userService.signup(signUpRequest), HttpStatus.OK);
    }

    /* 회원가입 시 닉네임 유효성 검사 */
    @ResponseBody
    @PostMapping("/validate")
    public ResponseEntity<BaseResponse> validateNickname(@RequestParam String nickname) throws InvalidNickNameException, NicknameAlreadyExistsException {
        return new ResponseEntity<>(userService.validateNickname(nickname), HttpStatus.OK);
    }

    /* 자신의 조회 - 마이페이지 */
    @ResponseBody
    @GetMapping("/{userIdx}/mypage")
    public ResponseEntity<BaseResponse> findUserProfile(@RequestBody SignUpRequest signUpRequest) throws InvalidNickNameException {
        return new ResponseEntity<>(userService.findUserProfile(signUpRequest), HttpStatus.OK);
    }

    /* 유저 검색 by 닉네임*/
    @ResponseBody
    @GetMapping("/search")
    public ResponseEntity<BaseResponse> search(@RequestParam("nickname") String nickname) {
        return new ResponseEntity<>(userService.search(nickname), HttpStatus.OK);
    }

    /* 해당 유저의 검색 기록 조회 */
    @ResponseBody
    @GetMapping("/{userIdx}/search-history")
    public ResponseEntity<BaseResponse> findSearchHistory(@PathVariable("userIdx") Long userIdx) {
        return new ResponseEntity<>(userService.findSearchHistory(userIdx), HttpStatus.OK);
    }

}
