package com.gurudev.aircnc.domain.member.entity;

import static com.gurudev.aircnc.util.AssertionUtil.assertThatAircncRuntimeException;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

class PasswordTest {

  @ParameterizedTest
  @CsvSource(value = {"12345678", "123456789012345"})
  void 비밀번호_생성_성공(String rawPassword) {
    Password password = new Password(rawPassword);

    assertThat(password).isEqualTo(new Password(rawPassword));
  }

  @ParameterizedTest
  @CsvSource(value = {"1234567", "1234567890123456"})
  void 비밀번호는_8자이상_15자이하(String invalidPassword) {
    assertThatAircncRuntimeException()
        .isThrownBy(() -> new Password(invalidPassword));
  }

  @Test
  void 비밀번호_암호화_테스트() {
    Password password = new Password("1234");

    password.encode(rawPassword1 -> "123456789012345");

    assertThat(password.getPassword()).isNotEqualTo("1234");
  }
}