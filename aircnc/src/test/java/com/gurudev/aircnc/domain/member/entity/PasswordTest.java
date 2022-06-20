package com.gurudev.aircnc.domain.member.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PasswordTest {

  @ParameterizedTest
  @CsvSource(value = {"12345678", "123456789012345"})
  void 비밀번호_생성_성공(String rawPassword) {
    Password password = new Password(rawPassword);

    assertThat(password.getPassword()).isEqualTo(rawPassword);
  }

  @ParameterizedTest
  @CsvSource(value = {"1234567", "1234567890123456"})
  void 비밀번호는_8자이상_15자이하(String invalidPassword) {
    assertThatIllegalArgumentException().isThrownBy(() -> new Password(invalidPassword));
  }
}