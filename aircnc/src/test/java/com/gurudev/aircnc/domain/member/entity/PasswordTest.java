package com.gurudev.aircnc.domain.member.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.gurudev.aircnc.infrastructure.security.PasswordEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PasswordTest {

  private final PasswordEncryptor passwordEncryptor = new PasswordEncryptor();

  @ParameterizedTest
  @CsvSource(value = {"12345678", "123456789012345"})
  void 비밀번호_생성_성공(String rawPassword) {
    //when
    Password password = new Password(rawPassword);

    //then
    assertThat(password).isEqualTo(new Password(rawPassword));
  }

  @ParameterizedTest
  @CsvSource(value = {"1234567", "1234567890123456"})
  void 비밀번호는_8자이상_15자이하(String invalidPassword) {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Password(invalidPassword));
  }

  @Test
  void 비밀번호_암호화_성공_테스트() {
    //given
    Password password = new Password("password");
    password.encode(passwordEncryptor);

    //when
    //then
    assertThatNoException().isThrownBy(
        () -> password.checkPassword(passwordEncryptor, new Password("password")));
  }

  @ParameterizedTest
  @CsvSource(value = {"12345678", "123456789012345"})
  void 암호화된_비밀번호_불일치_테스트(String rawPassword) {
    //given
    Password password = new Password(rawPassword);
    password.encode(passwordEncryptor);

    //then
    assertThatIllegalArgumentException().isThrownBy(
        () -> password.checkPassword(passwordEncryptor, new Password("invalidPassword")));
  }

  @Test
  void 암호화_되지_않은_비밀번호는_일치여부_확인불가() {
    //given
    Password password = new Password("nonEncrypted");

    //then
    assertThatIllegalStateException()
        .isThrownBy(() -> password.checkPassword(passwordEncryptor, new Password("nonEncrypted")));
  }
}