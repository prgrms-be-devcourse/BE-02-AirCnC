package com.gurudev.aircnc.domain.member.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static com.gurudev.aircnc.constant.Regex.EMAIL;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;

import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class Email {

  private static final Pattern pattern = Pattern.compile(EMAIL);

  @Column(unique = true, nullable = false)
  private String email;

  public Email(String email) {
    checkArgument(hasText(email), "이메일은 공백이 될 수 없습니다");
    checkArgument(pattern.matcher(email).find(), "이메일 형식이 잘못되었습니다");

    this.email = email;
  }

}
