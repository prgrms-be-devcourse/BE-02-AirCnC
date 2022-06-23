package com.gurudev.aircnc.configuration.jwt;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final Jwt jwt;

  public JwtAuthenticationFilter(Jwt jwt) {
    this.jwt = jwt;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      String token = getToken(request);
      if (token != null) {
        try {
          Jwt.Claims claims = verify(token);
          log.debug("Jwt parse result: {}", claims);

          String username = claims.username;
          List<GrantedAuthority> authorities = getAuthorities(claims);

          if (hasText(username) && authorities.size() > 0) {
            JwtAuthenticationToken authentication =
                new JwtAuthenticationToken(new JwtAuthenticationToken(token, username), null,
                    authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        } catch (Exception e) {
          log.warn("Jwt processing failed: {}", e.getMessage());
        }
      }
    } else {
      log.debug(
          "SecurityContextHolder not populated with security token, as it already contained: '{}'",
          SecurityContextHolder.getContext().getAuthentication());
    }

    filterChain.doFilter(request, response);
  }

  private String getToken(HttpServletRequest request) {
    String token = request.getHeader(AUTHORIZATION);
    if (hasText(token)) {
      log.debug("Jwt authorization api detected: {}", token);
      try {
        return URLDecoder.decode(token, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  private Jwt.Claims verify(String token) {
    return jwt.verify(token);
  }

  private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
    String[] roles = claims.roles;
    return roles == null || roles.length == 0 ?
        emptyList() :
        Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
  }

}
