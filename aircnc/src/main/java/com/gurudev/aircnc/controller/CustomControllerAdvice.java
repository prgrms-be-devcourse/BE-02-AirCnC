package com.gurudev.aircnc.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.gurudev.aircnc.exception.AircncRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomControllerAdvice {

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(AircncRuntimeException.class)
  public ErrorResponse handle(AircncRuntimeException ex) {
    return ErrorResponse.of(BAD_REQUEST, ex.getMessage());
  }

  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler(RuntimeException.class)
  public ErrorResponse handle(RuntimeException ex) {
    log.error(ex.getMessage(), ex);

    // TODO: 2022/06/25 이메일 혹은 슬랙으로 에러메시지 보내기

    return ErrorResponse.of(INTERNAL_SERVER_ERROR, "알 수 없는 에러가 발생했습니다. 고객센터에 문의하세요.");
  }

  @Getter
  @AllArgsConstructor
  public static class ErrorResponse {

    private int code;
    private String message;

    public static ErrorResponse of(HttpStatus status, String message) {
      return new ErrorResponse(status.value(), message);
    }
  }
}
