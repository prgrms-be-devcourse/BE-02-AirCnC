package com.gurudev.aircnc.configuration.jwt;

import lombok.RequiredArgsConstructor;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public final class JwtAuthentication {

  public final String token;
  public final Long id;

}
