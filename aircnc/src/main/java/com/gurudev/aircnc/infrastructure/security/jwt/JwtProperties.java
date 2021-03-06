package com.gurudev.aircnc.infrastructure.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "infrastructure.security.jwt")
public class JwtProperties {

  private String issuer;

  private String clientSecret;

  private int expirySeconds;
}
