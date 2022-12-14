package com.app.pingpong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 200 */
    SUCCESS(OK, "요청에 성공하였습니다."),
    SUCCESS_VALIDATE_NICKNAME(OK, "닉네임 검사에 성공하였습니다."),

    /* 400 : Bad Request   */
    USER_EMAIL_ALREADY_EXISTS(BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    USER_NICKNAME_ALREADY_EXISTS(BAD_REQUEST, "이미 사용 중인 닉네임입니다."),

    SEARCH_USER_NICKNAME_NOT_EXISTS(BAD_REQUEST, "해당 닉네임의 사용자가 존재하지않습니다."),

    EXCEED_HOST_TEAM_SIZE(BAD_REQUEST, "그룹을 더이상 생성할 수 없습니다. 그룹 개수는 최대 6개로 제한됩니다."),

    INVALID_NICKNAME(BAD_REQUEST, "형식에 맞지 않는 닉네임입니다."),
    INVALID_GROUP_MEMBER(BAD_REQUEST, "그룹 호스트(자기 자신)은/는 초대할 수 없습니다."),
    INVALID_SOCIAL_TYPE(BAD_REQUEST, "소셜로그인 타입의 형식을 확인해주세요."),
    INVALID_TEAM_MEMBER_SIZE(BAD_REQUEST, "그룹 멤버는 최소 1명, 최대 10명으로 제한됩니다."),
    INVALID_HOST(BAD_REQUEST, "해당 그룹의 호스트가 아니므로 방출 권한이 없습니다."),

    /* 404 : NOT FOUND */
    EMAIL_NOT_FOUND(NOT_FOUND, "존재하지 않는 이메일입니다, 회원가입을 해주세요."),
    USER_NOT_FOUND(NOT_FOUND, "존재하지 않는 유저입니다."),
    DELEGATOR_NOT_FOUND(NOT_FOUND, "존재하지 않는 유저이므로, 해당 유저에게 호스트를 위임할 수 없습니다."),
    TEAM_NOT_FOUND(NOT_FOUND, "존재하지 않는 그룹입니다."),

    /* 409 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 : INTERNAL SERVER ERROR */
    DATABASE_ERROR(INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패하였습니다."),
    INVALID_USER(INTERNAL_SERVER_ERROR, "로그인 되지 않은 유저입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
