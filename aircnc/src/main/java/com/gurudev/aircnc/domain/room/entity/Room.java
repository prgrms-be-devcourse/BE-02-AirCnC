package com.gurudev.aircnc.domain.room.entity;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static javax.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 숙소 */
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Room extends BaseIdEntity {

  public static final int ROOM_DESCRIPTION_MIN_LENGTH = 10;
  public static final int ROOM_PRICE_PER_DAY_MIN_VALUE = 10000;
  public static final int ROOM_CAPACITY_MIN_VALUE = 1;

  /* 이름 */
  private String name;

  /* 주소 */
  @Embedded
  private Address address;

  /* 설명 */
  private String description;

  /* 설명 */
  private int pricePerDay;

  /* 인원 수 */
  private int capacity;

  /* 호스트 */
  @ManyToOne(optional = false)
  private Member host;

  /* 리뷰 총 숫자 */
  private int reviewCount;

  @OneToMany(cascade = ALL)
  private List<RoomPhoto> roomPhotos = new ArrayList<>();

  public Room(String name, Address address, String description, int pricePerDay, int capacity) {
    setName(name);
    setDescription(description);
    setPricePerDay(pricePerDay);
    setCapacity(capacity);

    this.address = address;
    this.reviewCount = 0;
  }

  public void assignHost(Member host){
    setHost(host);
  }

  private void setHost(Member host) {
    checkArgument(host.getRole().equals(Role.HOST), "숙소 생성은 호스트만 할 수 있습니다");
    this.host = host;
  }

  private void setCapacity(int capacity) {
    checkArgument(capacity >= ROOM_CAPACITY_MIN_VALUE,
        "인원수는 %d명 이상이어야 합니다".formatted(ROOM_CAPACITY_MIN_VALUE));
    this.capacity = capacity;
  }

  private void setName(String name) {
    checkArgument(hasText(name), "이름은 공백이 될 수 없습니다");
    this.name = name;
  }

  private void setDescription(String description) {
    checkArgument(hasText(description), "설명은 공백이 될 수 없습니다");
    checkArgument(description.length() >= ROOM_DESCRIPTION_MIN_LENGTH,
        "설명은 %d 자 이상이어야 합니다".formatted(ROOM_DESCRIPTION_MIN_LENGTH));

    this.description = description;
  }

  private void setPricePerDay(int pricePerDay) {
    checkArgument(pricePerDay >= ROOM_PRICE_PER_DAY_MIN_VALUE,
        "가격은 %d원 이상이어야 합니다".formatted(ROOM_PRICE_PER_DAY_MIN_VALUE));
    this.pricePerDay = pricePerDay;
  }

  public void addRoomPhoto(RoomPhoto roomPhoto) {
    roomPhoto.updateRoom(this);
    roomPhotos.add(roomPhoto);
  }

  /* 숙소의 이름, 1박 당 가격, 설명을 변경 할 수 있다 */
  public Room update(String name, String description, Integer pricePerDay) {
    Optional.ofNullable(name).ifPresent(this::setName);
    Optional.ofNullable(description).ifPresent(this::setDescription);
    Optional.ofNullable(pricePerDay).ifPresent(this::setPricePerDay);

    return this;
  }

}
