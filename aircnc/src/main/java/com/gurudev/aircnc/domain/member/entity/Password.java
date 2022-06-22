package com.gurudev.aircnc.domain.member.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.util.StringUtils.hasText;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/* 비밀번호 */
@Getter
@Embeddable
@EqualsAndHashCode(of = "password")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

  @Column(nullable = false)
  public String password;

  @Transient
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Transient
  private boolean encoded;

  public Password(String password) {
    checkArgument(hasText(password), "비밀번호는 공백이 될 수 없습니다");
    checkArgument(password.length() >= 8 && password.length() <= 15, "비밀번호는 8자이상 15자 이하여야 합니다");

    this.password = password;
    this.encoded = false;
  }

  private Password(String password, PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(password);
    this.encoded = true;
  }

  public static String toString(Password password) {
    return password.getPassword();
  }

  public Password encode() {
    if (encoded) {
      return this;
    }

    encoded = true;
    return new Password(this.password, this.passwordEncoder);
  }

  public boolean matches(Password rawPassword) {
    if (!encoded) {
      throw new IllegalStateException("비밀번호가 암호화되지 않았습니다");
    }
    return passwordEncoder.matches(rawPassword.getPassword(), this.password);
  }
}
