package com.gurudev.aircnc.configuration;

import com.gurudev.aircnc.configuration.jwt.Jwt;
import com.gurudev.aircnc.configuration.resolver.LoginMemberArgumentResolver;
import com.gurudev.aircnc.domain.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final MemberService memberService;
  private final Jwt jwt;

  @Bean
  public HandlerMethodArgumentResolver memberArgumentResolver() {
    return new LoginMemberArgumentResolver(memberService, jwt);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(memberArgumentResolver());
  }
}
