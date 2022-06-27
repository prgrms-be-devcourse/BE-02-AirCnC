package com.gurudev.aircnc.domain.member.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import com.gurudev.aircnc.infrastructure.security.PasswordEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PasswordTest {

  private final PasswordEncryptor passwordEncryptor = new PasswordEncryptor();

  @ParameterizedTest
  @CsvSource(value = {"12345678", "123456789012345"})
  void 비밀번호_생성_성공(String rawPassword) {
    Password password = new Password(rawPassword);

    assertThat(password).isEqualTo(new Password(rawPassword));
  }

  @ParameterizedTest
  @CsvSource(value = {"1234567", "1234567890123456"})
  void 비밀번호는_8자이상_15자이하(String invalidPassword) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Password(invalidPassword));
  }

  @Test
  void 비밀번호_암호화_성공_테스트() {
    Password password = new Password("password");

    password.encode(passwordEncryptor);

    assertThat(password.matches(passwordEncryptor, new Password("password"))).isTrue();
  }

  @ParameterizedTest
  @CsvSource(value = {"12345678", "123456789012345"})
  void 암호화된_비밀번호_불일치_테스트(String rawPassword) {
    Password password = new Password(rawPassword);

    password.encode(passwordEncryptor);

    assertThat(password.matches(passwordEncryptor, new Password("invalidPassword"))).isFalse();
  }

  @Test
  void 암호화_되지_않은_비밀번호는_일치여부_확인불가() {
    Password password = new Password("abcdefggg");

    assertThatIllegalStateException()
        .isThrownBy(() -> password.matches(passwordEncryptor, new Password("abcdefggg")));
  }
}