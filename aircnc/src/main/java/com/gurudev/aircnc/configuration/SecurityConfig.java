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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

  private final JwtConfigure jwtConfigure;
  private final ApplicationContext applicationContext;

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    final JwtAuthenticationProvider jwtAuthenticationProvider = applicationContext.getBean(
        JwtAuthenticationProvider.class);

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
        .antMatchers("/api/v1/members", "/api/v1/login").permitAll()
        .anyRequest().authenticated();

    http.addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class);

    return http.build();
  }


  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    final Jwt jwt = applicationContext.getBean(Jwt.class);
    return new JwtAuthenticationFilter(jwtConfigure.getHeader(), jwt);
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
