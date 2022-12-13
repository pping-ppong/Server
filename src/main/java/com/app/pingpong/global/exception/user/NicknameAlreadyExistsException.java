package com.app.pingpong.global.exception.user;

import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.BaseResultCode;

public class NicknameAlreadyExistsException extends BaseException {
    public NicknameAlreadyExistsException() {
        super(BaseResultCode.USER_NICKNAME_ALREADY_EXISTS);
    }
}
