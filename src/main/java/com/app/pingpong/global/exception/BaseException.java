package com.app.pingpong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private ErrorCode errorCode;
}
