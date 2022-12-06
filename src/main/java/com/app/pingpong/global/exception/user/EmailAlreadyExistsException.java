package com.app.pingpong.global.exception.user;

import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.BaseExceptionStatus;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException() {
        super(BaseExceptionStatus.USER_EMAIL_ALREADY_EXISTS);
    }
}
