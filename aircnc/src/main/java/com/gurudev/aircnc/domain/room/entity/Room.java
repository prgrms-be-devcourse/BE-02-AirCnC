package com.gurudev.aircnc.domain.room.entity;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import com.gurudev.aircnc.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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

  private String name;

  @Embedded
  private Address address;

  @Lob
  private String description;

  private int pricePerDay;

  private int capacity;

  @ManyToOne(optional = false, fetch = LAZY)
  private Member host;

  @OneToMany(cascade = ALL)
  @JoinColumn(name = "room_id")
  private List<RoomPhoto> roomPhotos;

  public Room(String name, Address address, String description, int pricePerDay, int capacity,
      List<RoomPhoto> roomPhotos) {

    this.name = name;
    this.pricePerDay = pricePerDay;
    this.description = description;
    this.capacity = capacity;
    this.address = address;
    this.roomPhotos = roomPhotos;
  }

  public void assignHost(Member host) {
    checkArgument(host.isHost(), "숙소 생성은 호스트만 할 수 있습니다");

    this.host = host;
  }

  /**
   * 숙소의 이름, 1박 당 가격, 설명을 변경한다
   * <br> null 이라면 변경하지 않음
   */
  public Room update(String name, String description, Integer pricePerDay) {
    Optional.ofNullable(name).ifPresent(name1 -> this.name = name1);
    Optional.ofNullable(description).ifPresent(description1 -> this.description = description1);
    Optional.ofNullable(pricePerDay).ifPresent(pricePerDay1 -> this.pricePerDay = pricePerDay1);

    return this;
  }

  public boolean isOwnedBy(Member host) {
    return this.host.equals(host);
  }

}
