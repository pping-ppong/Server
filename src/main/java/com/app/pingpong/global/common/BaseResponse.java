package com.app.pingpong.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
@Builder
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    private final String code;
    private T result;

    public static <T> BaseResponse<T> ok(T result) {
        return (BaseResponse<T>) BaseResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message("요청에 성공하였습니다.")
                .result(result)
                .build();
    }

    public static <T> BaseResponse<T> ok() {
        return (BaseResponse<T>) BaseResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message("요청에 성공하였습니다.")
                .result(null)
                .build();
    }

}
