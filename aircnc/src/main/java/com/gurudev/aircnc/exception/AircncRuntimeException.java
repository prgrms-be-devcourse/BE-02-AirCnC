package com.gurudev.aircnc.exception;

public abstract class AircncRuntimeException extends RuntimeException{

  public AircncRuntimeException(String message) {
    super(message);
  }

  public AircncRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
