package com.app.pingpong.global.exception.user;

import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.BaseExceptionStatus;

public class NicknameAlreadyExistsException extends BaseException {
    public NicknameAlreadyExistsException() {
        super(BaseExceptionStatus.USER_NICKNAME_ALREADY_EXISTS);
    }
}
