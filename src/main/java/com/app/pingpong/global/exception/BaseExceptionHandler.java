package com.app.pingpong.global.exception;

import com.app.pingpong.global.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.app.pingpong.global.exception.ErrorCode.DUPLICATE_RESOURCE;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class })
    protected ResponseEntity<BaseResponse> handleDataException() {
        log.error("handleDataException throw Exception : {}", DUPLICATE_RESOURCE);
        return BaseResponse.toResponseEntity(DUPLICATE_RESOURCE);
    }

    @ExceptionHandler(value = { BaseException.class })
    protected ResponseEntity<BaseResponse> handleCustomException(BaseException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return BaseResponse.toResponseEntity(e.getErrorCode());
    }
}
