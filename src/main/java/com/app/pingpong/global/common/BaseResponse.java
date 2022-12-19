package com.app.pingpong.global.common;

import com.app.pingpong.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class BaseResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<BaseResponse> toResponseEntity(ErrorCode baseResultCode) {
        return ResponseEntity
                .status(baseResultCode.getHttpStatus())
                .body(BaseResponse.builder()
                        .status(baseResultCode.getHttpStatus().value())
                        .error(baseResultCode.getHttpStatus().name())
                        .code(baseResultCode.name())
                        .message(baseResultCode.getMessage())
                        .build());
    }
}
