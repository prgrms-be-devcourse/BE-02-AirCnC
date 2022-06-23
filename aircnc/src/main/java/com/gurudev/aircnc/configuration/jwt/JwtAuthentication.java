package com.gurudev.aircnc.configuration.jwt;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static org.springframework.util.StringUtils.hasText;

public class JwtAuthentication {

  public final String token;
  public final String email;

  JwtAuthentication(String token, String email) {
    checkArgument(hasText(token), "토큰은 null 일 수 없습니다.");
    checkArgument(hasText(email), "이메일은 null 일 수 없습니다.");

    this.token = token;
    this.email = email;
  }

}
