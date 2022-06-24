package com.gurudev.aircnc.domain.member.entity;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static com.gurudev.aircnc.constant.Regex.EMAIL;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;

import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 이메일
 * <li> RFC 5322 의 포맷으로 검증한다. @see <a href="https://datatracker.ietf.org/doc/html/rfc5322/">RFC 5322</a> </li>
 */
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

  public static String toString(Email email) {
    return email.getEmail();
  }

}
