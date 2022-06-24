package com.gurudev.aircnc.configuration;

import com.gurudev.aircnc.configuration.jwt.Jwt;
import com.gurudev.aircnc.configuration.jwt.JwtAuthenticationFilter;
import com.gurudev.aircnc.configuration.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private final Jwt jwt;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  @Override
  public void configure(HttpSecurity http) {
    http
        .authenticationProvider(jwtAuthenticationProvider)
        .addFilterAfter(new JwtAuthenticationFilter(jwt), SecurityContextPersistenceFilter.class);
  }
}
