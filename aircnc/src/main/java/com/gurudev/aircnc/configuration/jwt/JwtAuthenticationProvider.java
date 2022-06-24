package com.gurudev.aircnc.configuration.jwt;

import static org.springframework.util.TypeUtils.isAssignable;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.service.MemberService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final Jwt jwt;
  private final MemberService memberService;

  public JwtAuthenticationProvider(Jwt jwt, MemberService memberService) {
    this.jwt = jwt;
    this.memberService = memberService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
    return processUserAuthentication(
        String.valueOf(jwtAuthentication.getPrincipal()),
        jwtAuthentication.getCredentials()
    );
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return isAssignable(JwtAuthenticationToken.class, authentication);
  }

  private Authentication processUserAuthentication(String principal, String credentials) {
    try {
      Member member = memberService.login(new Email(principal), new Password(credentials));
      List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getRole().name()));
      String token = getToken(member.getId(), authorities);
      JwtAuthenticationToken authenticated =
          new JwtAuthenticationToken(
              new JwtAuthentication(token, member.getId()), null,
              authorities);
      authenticated.setDetails(member);
      return authenticated;
    } catch (IllegalArgumentException e) {
      throw new BadCredentialsException(e.getMessage());
    } catch (DataAccessException e) {
      throw new AuthenticationServiceException(e.getMessage(), e);
    }
  }

  private String getToken(final Long id, final List<GrantedAuthority> authorities) {
    String[] roles = authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .toArray(String[]::new);
    return jwt.sign(Jwt.Claims.from(id, roles));
  }
}
