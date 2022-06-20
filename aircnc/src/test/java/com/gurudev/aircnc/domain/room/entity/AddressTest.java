package com.gurudev.aircnc.domain.room.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class AddressTest {

  @Test
  void 주소_생성() {
    Address address = new Address("전라북도 전주시 완산구 풍산동 3가");

    assertThat(address).isEqualTo(new Address("전라북도 전주시 완산구 풍산동 3가"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 공백인_주소_생성_실패(String address) {

    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Address(address));
  }
}