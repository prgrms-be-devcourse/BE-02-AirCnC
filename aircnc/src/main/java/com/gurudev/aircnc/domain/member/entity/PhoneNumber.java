package com.gurudev.aircnc.domain.member.entity;

import static com.gurudev.aircnc.constant.Regex.PHONE_NUMBER;
import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static org.springframework.util.StringUtils.hasText;

import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 휴대폰 번호
 * <li> 01x-xxx(x)-xxxx 형식을 가진다. </li>
 */
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneNumber {

  private static final Pattern pattern = Pattern.compile(PHONE_NUMBER);

  @Column(nullable = false)
  private String phoneNumber;

  public PhoneNumber(String phoneNumber) {
    checkArgument(hasText(phoneNumber), "휴대폰 번호는 공백일 수 없습니다");
    checkArgument(pattern.matcher(phoneNumber).find(), "휴대폰 번호 형식이 잘못되었습니다");

    this.phoneNumber = phoneNumber;
  }

  public static String toString(PhoneNumber phoneNumber) {
    return phoneNumber.getPhoneNumber();
  }
}
