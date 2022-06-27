package com.gurudev.aircnc.domain.room.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class AddressTest {


  private String lotAddress;
  private String roadAddress;
  private String detailedAddress;
  private String postCode;

  @BeforeEach
  void setUp() {
    lotAddress = "전라북도 전주시 완산구 풍산동 123-1";
    roadAddress = "전라북도 전주시 완산구 풍산1로 1길";
    detailedAddress = "123호";
    postCode = "12345";
  }

  @Test
  void 주소_생성() {
    //when
    Address address = new Address(lotAddress, roadAddress, detailedAddress, postCode);

    //then
    assertThat(address).isEqualTo(new Address(lotAddress, roadAddress, detailedAddress, postCode));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 지번_주소가_공백인_주소_생성_실패(String lotAddress) {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Address(lotAddress, roadAddress, detailedAddress, postCode));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 도로명_주소가_공백인_주소_생성_실패(String roadAddress) {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Address(lotAddress, roadAddress, detailedAddress, postCode));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 상세_주소가_공백인_주소_생성_실패(String detailedAddress) {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Address(lotAddress, roadAddress, detailedAddress, postCode));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 우편번호가_공백인_주소_생성_실패(String postCode) {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Address(lotAddress, roadAddress, detailedAddress, postCode));
  }
}