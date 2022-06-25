package com.gurudev.aircnc.exception;

public class AircncRuntimeException extends RuntimeException {

  public AircncRuntimeException(String message) {
    super(message);
  }

  public AircncRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
