package com.gurudev.aircnc.configuration;

import com.gurudev.aircnc.configuration.jwt.Jwt;
import com.gurudev.aircnc.configuration.jwt.JwtAuthenticationFilter;
import com.gurudev.aircnc.configuration.jwt.JwtAuthenticationProvider;
import com.gurudev.aircnc.configuration.jwt.JwtConfigure;
import com.gurudev.aircnc.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtConfigure jwtConfigure;
  private final ApplicationContext ac;

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    JwtAuthenticationProvider jwtAuthenticationProvider = ac.getBean(
        JwtAuthenticationProvider.class);

    // REST API 애플리케이션이기 때문에 불필요한 기능 Disable
    http
        .formLogin().disable()
        .csrf().disable()
        .headers().disable()
        .httpBasic().disable()
        .rememberMe().disable()
        .logout().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(jwtAuthenticationProvider);

    http
        .authorizeHttpRequests()
        .antMatchers("/api/v1/members", "/api/v1/login").permitAll();
    // 현재 개발 단계 이므로 주석 처리
    //   .anyRequest().authenticated();

    http.addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class);

    return http.build();
  }

  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    Jwt jwt = ac.getBean(Jwt.class);
    return new JwtAuthenticationFilter(jwt);
  }

  @Bean
  public Jwt jwt() {
    return new Jwt(
        jwtConfigure.getIssuer(),
        jwtConfigure.getClientSecret(),
        jwtConfigure.getExpirySeconds()
    );
  }

  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider(MemberService memberService, Jwt jwt) {
    return new JwtAuthenticationProvider(jwt, memberService);
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
