package com.gurudev.aircnc.domain.trip.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.room.Room;
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

  public Trip(Member guest, Room room, LocalDate checkIn, LocalDate checkOut,
      int totalPrice, int headCount, TripStatus status) {
    checkArgument(checkOut.isAfter(checkIn), "체크아웃은 체크인 이전이 될 수 없습니다");
    checkArgument(!checkIn.isBefore(LocalDate.now()), "체크인 날짜는 오늘 이전이 될 수 없습니다");
    checkArgument(totalPrice >= 10000,
        "가경의 합은 %d원 미만이 될 수 없습니다".formatted(TRIP_TOTAL_PRICE_MIN_VALUE));
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

  public void chageStatus(TripStatus status) {
    this.status = status;
  }

}
