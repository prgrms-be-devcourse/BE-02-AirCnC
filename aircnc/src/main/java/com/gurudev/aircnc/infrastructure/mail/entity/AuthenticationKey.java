package com.gurudev.aircnc.infrastructure.mail.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationKey {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, length = 8, nullable = false)
  private String key;

  @Column(unique = true, nullable = false)
  private String email;

  public AuthenticationKey(String key, String email) {
    this.key = key;
    this.email = email;
  }

  public boolean validateKey(String key) {
    return this.key.equals(key);
  }

}
