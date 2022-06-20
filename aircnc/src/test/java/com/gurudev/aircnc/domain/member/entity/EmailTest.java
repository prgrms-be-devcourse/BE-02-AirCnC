package com.gurudev.aircnc.domain.member.entity;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmailTest {

  @Test
  void 이메일_생성_성공() {
    String testEmail = "test@email.com";

    Email email = new Email(testEmail);

    assertThat(email.getEmail()).isEqualTo(testEmail);
  }

  @ParameterizedTest
  @CsvSource(value = {"test.email.com", "@email.com"})
  void 이메일_생성_실패(String invalidEmail) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Email(invalidEmail));
  }
}