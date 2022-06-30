package com.gurudev.aircnc.infrastructure.mail.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class EmailAuthKey {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, length = 8, nullable = false)
  private String key;

  @Column(nullable = false)
  private String email;

  public EmailAuthKey(String key, String email) {
    this.key = key;
    this.email = email;
  }

  public boolean validateKey(String key) {
    return this.key.equals(key);
  }

}
