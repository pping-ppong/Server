package com.app.pingpong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseExceptionStatus {

    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    SUCCESS_VALIDATE_NICKNAME(true, 1001, "닉네임 검사에 성공하였습니다."),

    /* 3000   : Response Error  */
    USER_EMAIL_ALREADY_EXISTS(false, 3000, "이미 사용 중인 이메일입니다."),
    USER_NICKNAME_ALREADY_EXISTS(false, 3001, "이미 사용 중인 닉네임입니다."),

    INVALID_NICKNAME(false, 3001, "형식에 맞지 않는 닉네임입니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

}
