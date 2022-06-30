package com.gurudev.aircnc.controller;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

@NoArgsConstructor(access = PRIVATE)
public final class ApiResponse {

  public static <T> ResponseEntity<T> ok(@Nullable T body) {
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  public static <T> ResponseEntity<T> created(@Nullable T body) {
    return new ResponseEntity<>(body, HttpStatus.CREATED);
  }

  public static ResponseEntity<?> noContent() {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
