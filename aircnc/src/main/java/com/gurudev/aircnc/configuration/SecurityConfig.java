package com.gurudev.aircnc.configuration;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import com.gurudev.aircnc.configuration.jwt.Jwt;
import com.gurudev.aircnc.configuration.jwt.JwtAuthenticationFilter;
import com.gurudev.aircnc.configuration.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final Jwt jwt;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    return http
        // REST API 애플리케이션이기 때문에 불필요한 기능 Disable
        .formLogin().disable()
        .csrf().disable()
        .headers().disable()
        .httpBasic().disable()
        .rememberMe().disable()
        .logout().disable()
        .requestCache().disable()
        .anonymous().disable()

        .authenticationProvider(jwtAuthenticationProvider)

        .authorizeRequests(authz -> authz.antMatchers("/api/v1/members", "/api/v1/login").permitAll())

        .sessionManagement().sessionCreationPolicy(STATELESS)

        .and().addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)

        .build();
  }

  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwt);
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
