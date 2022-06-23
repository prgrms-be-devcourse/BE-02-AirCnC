package com.gurudev.aircnc.configuration.resolver;

import com.gurudev.aircnc.configuration.jwt.Jwt;
import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

  private final MemberService memberService;
  private final Jwt jwt;

  public LoginMemberArgumentResolver(MemberService memberService, Jwt jwt) {
    this.memberService = memberService;
    this.jwt = jwt;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(LoginMember.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    String token = Tokens.get((HttpServletRequest) webRequest.getNativeRequest());

    if (token != null) {
      try {
        Jwt.Claims claims = jwt.verify(token);

        String username = claims.getUsername();

        return memberService.getByEmail(new Email(username));

      } catch (Exception e) {
        return null;
      }

    }

    return null;
  }
}
