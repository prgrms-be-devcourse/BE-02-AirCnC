package com.gurudev.aircnc.configuration.jwt;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
//@Setter
//@EnableConfigurationProperties
//@ConfigurationProperties(prefix = "jwt")
public class JwtConfigure {

  private String header = "token";

  private String issuer = "gurudev";

  private String clientSecret = "EENY5W0eegTf1naQB2eDeyCLl5kRS2b8xa5c4qLdS0hmVjtbvo8tOyhPMcAmtPuQ";

  private int expirySeconds = 60;
}
