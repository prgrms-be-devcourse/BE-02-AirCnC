package com.gurudev.aircnc.domain.room.entity;


import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;

import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 주소 */
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class Address {

  private String address;

  public Address(String address) {
    checkArgument(hasText(address), "주소는 공백이 될 수 없습니다");

    this.address = address;
  }
}
