package com.gurudev.aircnc.domain.member.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.util.StringUtils.hasText;

import java.util.function.Function;
import java.util.function.UnaryOperator;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 비밀번호 */
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

  @Column(nullable = false)
  public String password;

  public Password(String password) {
    checkArgument(hasText(password), "비밀번호는 공백이 될 수 없습니다");
    checkArgument(password.length() >= 8 && password.length() <= 15, "비밀번호는 8자이상 15자 이하여야 합니다");

    this.password = password;
  }

  public static String toString(Password password) {
    return password.getPassword();
  }

  public void encode(UnaryOperator<String> encoder) {
    this.password = encoder.apply(this.password);
  }
}
