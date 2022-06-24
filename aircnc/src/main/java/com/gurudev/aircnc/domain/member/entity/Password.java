package com.gurudev.aircnc.domain.member.entity;

import static com.gurudev.aircnc.constant.Regex.PASSWORD_ENCODING_PREFIX;
import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static org.springframework.util.StringUtils.hasText;

import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/* 비밀번호 */
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

  public static final Pattern ENCODED_PATTERN = Pattern.compile(PASSWORD_ENCODING_PREFIX);

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

  public void encode(PasswordEncryptor encoder) {
    this.password = encoder.encrypt(this.password);
  }

  public boolean matches(PasswordMatcher matcher, Password rawPassword) {
    if (!isEncoded()) {
      throw new IllegalStateException("비밀번호가 암호화되지 않았습니다");
    }
    return matcher.match(rawPassword.getPassword(), this.password);
  }

  public boolean isEncoded() {
    return ENCODED_PATTERN.matcher(this.password).find();
  }

}
