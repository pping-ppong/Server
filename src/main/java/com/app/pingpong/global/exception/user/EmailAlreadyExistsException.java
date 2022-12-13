package com.app.pingpong.global.exception.user;

import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.BaseResultCode;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException() {
        super(BaseResultCode.USER_EMAIL_ALREADY_EXISTS);
    }
}
