package com.app.pingpong.global.common.response;

import com.app.pingpong.global.common.exception.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static com.app.pingpong.global.common.exception.StatusCode.SUCCESS;

@Getter
@Builder
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "type", "message", "result"})
public class BaseResponse<T> {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final int code;
    private final String message;
    private final String type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public BaseResponse(T result) {
        this.isSuccess = true;
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.type = SUCCESS.getType();
        this.result = result;
    }

    public BaseResponse(StatusCode status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
        this.type = status.getType();
    }

    public BaseResponse(StatusCode status, T result) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
        this.type = status.getType();
        this.result = result;
    }
}
