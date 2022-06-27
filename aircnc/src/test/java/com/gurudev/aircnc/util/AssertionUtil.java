package com.gurudev.aircnc.util;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.gurudev.aircnc.exception.AircncRuntimeException;
import com.gurudev.aircnc.exception.NotFoundException;
import org.assertj.core.api.ThrowableTypeAssert;

public class AssertionUtil {

  public static ThrowableTypeAssert<AircncRuntimeException> assertThatAircncRuntimeException() {
    return assertThatExceptionOfType(AircncRuntimeException.class);
  }

  public static ThrowableTypeAssert<NotFoundException> assertThatNotFoundException() {
    return assertThatExceptionOfType(NotFoundException.class);
  }
}
