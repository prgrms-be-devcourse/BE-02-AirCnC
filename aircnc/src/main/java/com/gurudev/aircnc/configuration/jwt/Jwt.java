package com.gurudev.aircnc.configuration.jwt;

import static lombok.AccessLevel.PRIVATE;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public final class Jwt {

  private final String issuer;
  private final int expirySeconds;
  private final Algorithm algorithm;
  private final JWTVerifier jwtVerifier;

  public Jwt(JwtProperties jwtConfigure) {
    this.issuer = jwtConfigure.getIssuer();
    String clientSecret = jwtConfigure.getClientSecret();
    this.expirySeconds = jwtConfigure.getExpirySeconds();
    this.algorithm = Algorithm.HMAC512(clientSecret);
    this.jwtVerifier = com.auth0.jwt.JWT.require(algorithm)
                                        .withIssuer(issuer)
                                        .build();
  }

  public String sign(Claims claims) {
    Date now = new Date();
    long validity = expirySeconds * 1000L;

    return com.auth0.jwt.JWT.create()
                            .withIssuer(issuer)
                            .withIssuedAt(now)
                            .withExpiresAt(new Date(now.getTime() + validity))
                            .withClaim("id", claims.id)
                            .withArrayClaim("roles", claims.roles)
                            .sign(algorithm);
  }

  public Claims verify(String token) throws JWTVerificationException {
    return new Claims(jwtVerifier.verify(token));
  }

  @NoArgsConstructor(access = PRIVATE)
  public static class Claims {

    Long id;
    String[] roles;
    Date iat;
    Date exp;

    Claims(DecodedJWT decodedJWT) {
      Claim id = decodedJWT.getClaim("id");
      if (!id.isNull()) {
        this.id = id.asLong();
      }

      Claim roles = decodedJWT.getClaim("roles");
      if (!roles.isNull()) {
        this.roles = roles.asArray(String.class);
      }

      this.iat = decodedJWT.getIssuedAt();
      this.exp = decodedJWT.getExpiresAt();
    }

    public static Claims from(Long id, String[] roles) {
      Claims claims = new Claims();
      claims.id = id;
      claims.roles = roles;
      return claims;
    }
  }
}
