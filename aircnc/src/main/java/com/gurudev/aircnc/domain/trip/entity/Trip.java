package com.gurudev.aircnc.domain.trip.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static java.time.LocalDate.now;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.room.entity.Room;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Trip extends BaseIdEntity {

  public static final int TRIP_TOTAL_PRICE_MIN_VALUE = 10000;
  public static final int TRIP_HEADCOUNT_MIN_VALUE = 1;

  @ManyToOne(fetch = LAZY)
  private Member guest;

  @ManyToOne(fetch = LAZY)
  private Room room;

  private LocalDate checkIn;

  private LocalDate checkOut;

  private int totalPrice;

  private int headCount;

  private TripStatus status;

  private Trip(Member guest, Room room, LocalDate checkIn, LocalDate checkOut,
      int totalPrice, int headCount, TripStatus status) {
    checkArgument(checkOut.isAfter(checkIn), "체크아웃은 체크인 이전이 될 수 없습니다");
    checkArgument(checkIn.isEqual(now()) || checkIn.isAfter(now()),
        "체크인 날짜는" + now() + " 이전이 될 수 없습니다.");
    checkArgument(totalPrice >= 10000,
        "총 가격은 %d원 미만이 될 수 없습니다".formatted(TRIP_TOTAL_PRICE_MIN_VALUE));
    checkArgument(headCount >= 1,
        "인원은 %d명 이상이여야 합니다".formatted(TRIP_HEADCOUNT_MIN_VALUE));

    this.guest = guest;
    this.room = room;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
    this.totalPrice = totalPrice;
    this.headCount = headCount;
    this.status = status;
  }

  public static Trip ofReserved(Member guest, Room room, LocalDate checkIn, LocalDate checkOut,
      int totalPrice, int headCount) {
    return new Trip(guest, room, checkIn, checkOut, totalPrice, headCount, RESERVED);
  }

  public void changeStatus(TripStatus status) {
    this.status = status;
  }

}
