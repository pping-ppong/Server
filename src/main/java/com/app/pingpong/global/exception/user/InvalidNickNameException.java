package com.app.pingpong.global.exception.user;

import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.BaseResultCode;

public class InvalidNickNameException extends BaseException {
    public InvalidNickNameException() {
        super(BaseResultCode.INVALID_NICKNAME);
    }
}
