package com.gurudev.aircnc.exception;

import javax.annotation.CheckForNull;

public class Preconditions {

  public static void checkArgument(boolean expression, @CheckForNull Object errorMessage) {
    if (!expression) {
      throw new AircncRuntimeException(String.valueOf(errorMessage));
    }
  }

  public static <T> void checkNotNull(@CheckForNull T reference,
      @CheckForNull Object errorMessage) {
    if (reference == null) {
      throw new AircncRuntimeException(String.valueOf(errorMessage));
    }
  }
}
