package com.gurudev.aircnc.domain.member.entity;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmailTest {

  @Test
  void 이메일_생성_성공() {
    //given
    String emailString = "test@email.com";

    //when
    Email email = new Email(emailString);

    //then
    assertThat(email).isEqualTo(new Email(emailString));
  }

  @ParameterizedTest
  @CsvSource(value = {"test.email.com", "@email.com"})
  void 이메일_생성_실패(String invalidEmail) {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Email(invalidEmail));
  }
}