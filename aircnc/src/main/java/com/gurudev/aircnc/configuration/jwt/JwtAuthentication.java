package com.gurudev.aircnc.configuration.jwt;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static org.springframework.util.StringUtils.hasText;

public class JwtAuthentication {
  public final String token;

  public final String username;

  JwtAuthentication(String token, String username) {
    checkArgument(hasText(token), "token must be provided.");
    checkArgument(hasText(username), "username must be provided.");

    this.token = token;
    this.username = username;
  }

}
