package com.gurudev.aircnc.domain.room.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class AddressTest {

  private final String lotAddress = "전라북도 전주시 완산구 풍산동 123-1";
  private final String roadAddress = "전라북도 전주시 완산구 풍산1로 1길";
  private final String detailedAddress = "123호";
  private final String postCode = "12345";

  @Test
  void 주소_생성() {
    Address address = new Address(lotAddress, roadAddress, detailedAddress, postCode);

    assertThat(address).isEqualTo(new Address(lotAddress, roadAddress, detailedAddress, postCode));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 지번_주소가_공백인_주소_생성_실패(String lotAddress) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Address(lotAddress, roadAddress, detailedAddress, postCode));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 도로명_주소가_공백인_주소_생성_실패(String roadAddress) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Address(lotAddress, roadAddress, detailedAddress, postCode));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 상세_주소가_공백인_주소_생성_실패(String roadAddress) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Address(lotAddress, roadAddress, detailedAddress, postCode));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 우편번호가_공백인_주소_생성_실패(String postCode) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Address(lotAddress, roadAddress, detailedAddress, postCode));
  }
}