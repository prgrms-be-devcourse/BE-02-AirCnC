package com.gurudev.aircnc.exception;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class Preconditions {

  public static void checkArgument(boolean expression, Object errorMessage) {
    if (!expression) {
      throw new IllegalArgumentException(String.valueOf(errorMessage));
    }
  }

  public static void checkCondition(boolean condition, Object errorMessage) {
    if (!condition) {
      throw new AircncRuntimeException(String.valueOf(errorMessage));
    }
  }

  public static void checkState(boolean expression, Object errorMessage) {
    if (!expression) {
      throw new IllegalStateException(String.valueOf(errorMessage));
    }
  }

  public static <T> void checkNotNull(T reference, Object errorMessage) {
    if (reference == null) {
      throw new IllegalArgumentException(String.valueOf(errorMessage));
    }
  }
}
