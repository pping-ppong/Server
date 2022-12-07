package com.app.pingpong.global.exception.user;

import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.BaseExceptionStatus;

public class InvalidNickNameException extends BaseException {
    public InvalidNickNameException() {
        super(BaseExceptionStatus.INVALID_NICKNAME);
    }
}
