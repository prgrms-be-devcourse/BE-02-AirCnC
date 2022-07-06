package com.gurudev.aircnc.infrastructure.mail.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
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
  private String authKey;

  @Column(nullable = false)
  private String email;

  private LocalDateTime createdAt;

  public EmailAuthKey(String authKey, String email) {
    this.authKey = authKey;
    this.email = email;
    this.createdAt = LocalDateTime.now();
  }

  public boolean validateKey(String key) {
    return this.authKey.equals(key);
  }

}
