package com.gurudev.aircnc.configuration.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigure {

  private String issuer;

  private String clientSecret;

  private int expirySeconds;
}
