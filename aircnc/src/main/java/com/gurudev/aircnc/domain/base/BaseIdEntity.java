package com.gurudev.aircnc.domain.base;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@MappedSuperclass
public abstract class BaseIdEntity {

  @Id
  @GeneratedValue
  private Long id;

}
