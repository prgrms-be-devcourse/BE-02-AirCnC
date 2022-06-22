package com.gurudev.aircnc.exception;

public class NotFoundException extends AircncRuntimeException{

  public NotFoundException(Class<?> clazz) {
    super(clazz.getSimpleName() + "(이)가 존재하지 않습니다");
  }

  public NotFoundException(Class<?> clazz, String arg) {
    super(arg + "에 해당하는 " + clazz.getSimpleName() + "(이)가 존재하지 않습니다");
  }
}
