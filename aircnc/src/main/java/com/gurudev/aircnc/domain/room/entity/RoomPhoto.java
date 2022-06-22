package com.gurudev.aircnc.domain.room.entity;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 숙소 사진 */
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(callSuper = false, of = "fileName")
public class RoomPhoto extends BaseIdEntity {

  private String fileName;

  @ManyToOne(optional = false, fetch = LAZY)
  private Room room;

  public RoomPhoto(String fileName) {
    checkArgument(hasText(fileName), "파일 이름은 공백이 될 수 없습니다");

    this.fileName = fileName;
  }

  public void updateRoom(Room room) {
    this.room = room;
  }

}
