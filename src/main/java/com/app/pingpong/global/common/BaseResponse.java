package com.app.pingpong.global.common;

import com.app.pingpong.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.app.pingpong.global.exception.ErrorCode.SUCCESS;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {
    private final HttpStatus code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public BaseResponse(T result) {
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getHttpStatus();
        this.result = result;
    }

    public BaseResponse(ErrorCode status) {
        this.message = status.getMessage();
        this.code = status.getHttpStatus();
    }
}
