package com.gurudev.aircnc.configuration.jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.Map;

public final class Jwt {

  private final String issuer;
  private final String clientSecret;
  private final int expirySeconds;
  private final Algorithm algorithm;
  private final JWTVerifier jwtVerifier;

  public Jwt(String issuer, String clientSecret, int expirySeconds) {
    this.issuer = issuer;
    this.clientSecret = clientSecret;
    this.expirySeconds = expirySeconds;
    this.algorithm = Algorithm.HMAC512(clientSecret);
    this.jwtVerifier = com.auth0.jwt.JWT.require(algorithm)
        .withIssuer(issuer)
        .build();
  }

  public String sign(Claims claims) {
    Date now = new Date();
    JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
    builder.withIssuer(issuer);
    builder.withIssuedAt(now);
    if (expirySeconds > 0) {
      builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1000));
    }
    builder.withClaim("id", claims.id);
    builder.withArrayClaim("roles", claims.roles);
    return builder.sign(algorithm);
  }

  public Claims verify(String token) throws JWTVerificationException {
    return new Claims(jwtVerifier.verify(token));
  }

  public String getIssuer() {
    return issuer;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public int getExpirySeconds() {
    return expirySeconds;
  }

  public Algorithm getAlgorithm() {
    return algorithm;
  }

  public JWTVerifier getJwtVerifier() {
    return jwtVerifier;
  }

  public static class Claims {

    Long id;
    String[] roles;
    Date iat;
    Date exp;

    private Claims() {/*no-op*/}

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

    public Map<String, Object> asMap() {
      return Map.of("username", id,
          "roles", roles,
          "iat", iat(),
          "exp", exp());
    }

    public Long getUsername() {
      return this.id;
    }

    public String[] getRoles() {
      return this.roles;
    }

    long iat() {
      return iat != null ? iat.getTime() : -1;
    }

    long exp() {
      return exp != null ? exp.getTime() : -1;
    }

    void eraseIat() {
      iat = null;
    }

    void eraseExp() {
      exp = null;
    }

  }

}
