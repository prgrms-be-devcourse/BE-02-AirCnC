package com.gurudev.aircnc.domain.room.entity;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import com.gurudev.aircnc.domain.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 숙소
 * <li> 숙소 생성은 호스트만 할 수 있습니다 </li>
 * <li> 숙소는 한장 이상의 사진을 가져야 합니다</li>
 */
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Room extends BaseIdEntity {

  public static final int ROOM_DESCRIPTION_MIN_LENGTH = 10;
  public static final int ROOM_PRICE_PER_DAY_MIN_VALUE = 10000;

  private String name;

  @Embedded
  private Address address;

  @Lob
  private String description;

  private int pricePerDay;

  private int capacity;

  @ManyToOne(optional = false, fetch = LAZY)
  private Member host;

  private int reviewCount;

  @OneToMany(cascade = ALL, mappedBy = "room")
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
    checkArgument(host.isHost(), "숙소 생성은 호스트만 할 수 있습니다");

    this.host = host;
  }

  private void setCapacity(int capacity) {
    checkArgument(capacity >= 1, "인원수는 한명 이상이어야 합니다");

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
    roomPhotos.add(roomPhoto);
  }

  /**
   * 숙소의 이름, 1박 당 가격, 설명을 변경한다 <br>
   * null 이라면 변경하지 않음
   */
  public Room update(String name, String description, Integer pricePerDay) {
    Optional.ofNullable(name).ifPresent(this::setName);
    Optional.ofNullable(description).ifPresent(this::setDescription);
    Optional.ofNullable(pricePerDay).ifPresent(this::setPricePerDay);

    return this;
  }

}
