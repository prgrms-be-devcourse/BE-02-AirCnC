package com.gurudev.aircnc.configuration.resolver;

import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Tokens {

  public static String get(HttpServletRequest request){
    return request.getHeader(HttpHeaders.AUTHORIZATION);
  }

}
