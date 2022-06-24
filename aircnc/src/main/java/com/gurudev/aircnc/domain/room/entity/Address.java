package com.gurudev.aircnc.domain.room.entity;


import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;

import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주소
 * <li> 지번 주소, 도로명 주소, 상세 주소, 우편 번호를 가진다 </li>
 */
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class Address {

  private String lotAddress;

  private String roadAddress;

  private String detailedAddress;

  private String postCode;

  public Address(String lotAddress, String roadAddress, String detailedAddress, String postCode) {
    checkArgument(hasText(lotAddress), "지번 주소는 공백이 될 수 없습니다");
    checkArgument(hasText(roadAddress), "도로명 주소는 공백이 될 수 없습니다");
    checkArgument(hasText(detailedAddress), "상세 주소는 공백이 될 수 없습니다");
    checkArgument(hasText(postCode), "우변 번호는 공백이 될 수 없습니다");

    this.lotAddress = lotAddress;
    this.roadAddress = roadAddress;
    this.detailedAddress = detailedAddress;
    this.postCode = postCode;
  }

  public static String toString(Address address) {
    return String.join(" ", address.roadAddress, address.detailedAddress);
  }
}
