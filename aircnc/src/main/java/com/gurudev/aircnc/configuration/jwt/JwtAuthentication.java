package com.gurudev.aircnc.configuration.jwt;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

public class JwtAuthentication {

  public final String token;
  public final Long id;

  JwtAuthentication(String token, Long id) {
    checkArgument(hasText(token), "토큰은 null 일 수 없습니다.");
    checkArgument(nonNull(id), "id는 null일 수 없습니다.");

    this.token = token;
    this.id = id;
  }

}
