package com.gurudev.aircnc.domain.member.entity;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PhoneNumberTest {

  @ParameterizedTest
  @CsvSource(value = {"010-123-4567", "010-1234-5678"})
  void 전화번호_생성_성공(String testPhoneNumber) {
    //when
    PhoneNumber phoneNumber = new PhoneNumber(testPhoneNumber);

    //then
    assertThat(phoneNumber).isEqualTo(new PhoneNumber(testPhoneNumber));
  }

  @ParameterizedTest
  @CsvSource(value = {"0101234567", "010-12345678", "0-1234-5678", "070-123-4567"})
  void 전화번호_생성_실패(String testPhoneNumber) {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new PhoneNumber(testPhoneNumber));
  }
}
