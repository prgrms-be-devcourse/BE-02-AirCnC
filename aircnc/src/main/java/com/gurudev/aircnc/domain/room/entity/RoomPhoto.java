package com.gurudev.aircnc.domain.room.entity;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class RoomPhoto extends BaseIdEntity {

  private String fileName;

  public RoomPhoto(String fileName) {
    checkArgument(hasText(fileName), "파일 이름은 공백이 될 수 없습니다");

    this.fileName = fileName;
  }
}
