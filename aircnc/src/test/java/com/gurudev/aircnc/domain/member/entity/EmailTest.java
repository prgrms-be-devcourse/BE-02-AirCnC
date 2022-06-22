package com.gurudev.aircnc.domain.member.entity;


import static com.gurudev.aircnc.util.AssertionUtil.assertThatAircncRuntimeException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmailTest {

  @Test
  void 이메일_생성_성공() {
    Email email = new Email("test@email.com");

    assertThat(email).isEqualTo(new Email("test@email.com"));
  }

  @ParameterizedTest
  @CsvSource(value = {"test.email.com", "@email.com"})
  void 이메일_생성_실패(String invalidEmail) {
    assertThatAircncRuntimeException()
        .isThrownBy(() -> new Email(invalidEmail));
  }
}