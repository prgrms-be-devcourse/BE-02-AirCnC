package com.gurudev.aircnc.domain.room.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class RoomPhoto extends BaseIdEntity {

  private String photoUrl;

  @ManyToOne(optional = false, fetch = LAZY)
  private Room room;

  public RoomPhoto(String photoUrl, Room room) {
    checkArgument(hasText(photoUrl), "사진의 주소는 공백이 될 수 없습니다");

    this.photoUrl = photoUrl;
    this.room = room;
  }

}
