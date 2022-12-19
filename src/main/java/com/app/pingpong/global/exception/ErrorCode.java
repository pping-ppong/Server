package com.app.pingpong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 200 */
    //SUCCESS_BASE(SUCCESS, "요청에 성공하였습니다."),
    //SUCCESS_VALIDATE_NICKNAME(SUCCESS, "닉네임 검사에 성공하였습니다."),

    /* 400 : Bad Request   */
    USER_EMAIL_ALREADY_EXISTS(BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    USER_NICKNAME_ALREADY_EXISTS(BAD_REQUEST, "이미 사용 중인 닉네임입니다."),
    SEARCH_USER_NICKNAME_NOT_EXISTS(BAD_REQUEST, "해당 닉네임의 사용자가 존재하지않습니다."),
    INVALID_NICKNAME(BAD_REQUEST, "형식에 맞지 않는 닉네임입니다."),

    /* 404 : NOT FOUND */
    EMAIL_NOT_FOUND(NOT_FOUND, "존재하지 않는 이메일입니다."),


    /* 409 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 : INTERNAL SERVER ERROR */
    DATABASE_ERROR(INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패하였습니다."),
    INVALID_USER(INTERNAL_SERVER_ERROR, "로그인 되지 않은 유저입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
