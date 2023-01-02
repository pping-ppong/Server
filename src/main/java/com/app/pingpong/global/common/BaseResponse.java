package com.app.pingpong.global.common;

import com.app.pingpong.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.app.pingpong.global.exception.ErrorCode.SUCCESS;

@Getter
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T> {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final HttpStatus code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public BaseResponse(T result) {
        this.isSuccess = true;
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getHttpStatus();
        this.result = result;
    }

    public static ResponseEntity<BaseResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.builder()
                        .isSuccess(false)
                        .code(errorCode.getHttpStatus())
                        .message(errorCode.getMessage())
                        .build()
                );
    }
}
