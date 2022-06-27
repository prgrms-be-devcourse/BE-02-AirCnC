package com.gurudev.aircnc.domain.member.entity;

import static com.gurudev.aircnc.constant.Regex.PASSWORD_ENCODING_PREFIX;
import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static com.gurudev.aircnc.exception.Preconditions.checkStatement;
import static org.springframework.util.StringUtils.hasText;

import com.gurudev.aircnc.infrastructure.security.PasswordEncryptor;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비밀번호
 * <li> 8자 ~ 15자의 길이를 가져야 한다. </li>
 */
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
    this.password = encoder.encode(this.password);
  }

  public boolean matches(PasswordEncryptor encryptor, Password rawPassword) {
    checkStatement(ENCODED_PATTERN.matcher(this.password).find(), "비밀번호가 암호화되지 않았습니다");

    return encryptor.matches(rawPassword.getPassword(), this.password);
  }

}
