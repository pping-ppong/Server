package com.app.pingpong.global.common;

import com.app.pingpong.global.exception.BaseException;
import com.app.pingpong.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static com.app.pingpong.global.exception.ErrorCode.SUCCESS;

@Getter
@AllArgsConstructor
@Builder
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

    public static ResponseEntity<BaseResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.builder()
                        .code(errorCode.getHttpStatus())
                        .message(errorCode.getMessage())
                        .build()
                );
    }
}
