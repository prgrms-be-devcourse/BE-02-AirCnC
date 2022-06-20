package com.gurudev.aircnc.domain.room;

import static com.google.common.base.Preconditions.checkArgument;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import com.gurudev.aircnc.domain.member.entity.Member;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Room extends BaseIdEntity {

  private String name;

  @Embedded
  private Address address;

  private String description;

  private int pricePerDay;

  private int capacity;

  @ManyToOne(optional = false)
  private Member host;

  private int reviewCount;

  public Room(String name, Address address, String description, int pricePerDay, int capacity,
      Member host) {
    checkArgument(hasText(name), "이름은 공백이 될 수 없습니다");
    checkArgument(name.length() >=1, "이름은 1자 이상이어야 합니다");
    checkArgument(hasText(description), "설명은 공백이 될 수 없습니다");
    checkArgument(description.length() >=10, "설명은 10자 이상이어야 합니다");
    checkArgument(pricePerDay >= 10000, "가격은 10000원 이상이어야 합니다");
    checkArgument(capacity>=1, "인원수는 1명 이상이어야 합니다");

    this.name = name;
    this.address = address;
    this.description = description;
    this.pricePerDay = pricePerDay;
    this.capacity = capacity;
    this.host = host;
    this.reviewCount = 0;
  }
}
