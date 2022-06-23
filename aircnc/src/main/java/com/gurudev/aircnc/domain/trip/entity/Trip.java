package com.gurudev.aircnc.domain.trip.entity;

import static com.gurudev.aircnc.domain.trip.entity.TripStatus.CANCELLED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static java.time.LocalDate.now;
import static java.time.Period.between;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.exception.TripCancelException;
import com.gurudev.aircnc.exception.TripReservationException;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 여행 */
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Trip extends BaseIdEntity {

  public static final int TRIP_TOTAL_PRICE_MIN_VALUE = 10000;
  public static final int TRIP_HEADCOUNT_MIN_VALUE = 1;

  /* 게스트 */
  @ManyToOne(fetch = LAZY)
  private Member guest;

  /* 숙소 */
  @ManyToOne(fetch = LAZY)
  private Room room;

  /* 체크인 */
  private LocalDate checkIn;

  /* 체크아웃 */
  private LocalDate checkOut;

  /* 총 가격 */
  private int totalPrice;

  /* 인원 수 */
  private int headCount;

  /* 상태 */
  private TripStatus status;

  private Trip(Member guest, Room room, LocalDate checkIn, LocalDate checkOut,
      int totalPrice, int headCount, TripStatus status) {
    checkArgument(checkOut.isAfter(checkIn), "체크아웃은 체크인 이전이 될 수 없습니다");
    checkArgument(checkIn.isEqual(now()) || checkIn.isAfter(now()),
        "체크인 날짜는" + now() + " 이전이 될 수 없습니다.");

    checkArgument(totalPrice >= TRIP_TOTAL_PRICE_MIN_VALUE,
        "총 가격은 %d원 미만이 될 수 없습니다".formatted(TRIP_TOTAL_PRICE_MIN_VALUE));
    checkTotalPrice(checkIn, checkOut, totalPrice, room);
    checkHeadCount(headCount, room);

    checkArgument(headCount >= TRIP_HEADCOUNT_MIN_VALUE,
        "인원은 %d명 이상이여야 합니다".formatted(TRIP_HEADCOUNT_MIN_VALUE));

    this.guest = guest;
    this.room = room;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
    this.totalPrice = totalPrice;
    this.headCount = headCount;
    this.status = status;

  }

  /* 예약 상태인 여행 생성 */
  public static Trip ofReserved(Member guest, Room room, LocalDate checkIn, LocalDate checkOut,
      int totalPrice, int headCount) {
    return new Trip(guest, room, checkIn, checkOut, totalPrice, headCount, RESERVED);
  }

  private void checkHeadCount(int headCount, Room room) {
    if (room.getCapacity() < headCount) {
      throw new TripReservationException("인원 수가 유효하지 않습니다");
    }
  }

  /* 서버의 총 가격과 요청 총 가격 검증 */
  private void checkTotalPrice(LocalDate checkIn, LocalDate checkOut, int totalPrice, Room room) {
    int calculatedTotalPrice = getDays(checkIn, checkOut) * room.getPricePerDay();
    if (totalPrice != calculatedTotalPrice) {
      throw new TripReservationException("총 가격이 유효하지 않습니다");
    }
  }

  private int getDays(LocalDate from, LocalDate to) {
    return between(from, to).getDays();
  }

  public void changeStatus(TripStatus status) {
    this.status = status;
  }

  public void cancel(){
    if(status != RESERVED){
      throw new TripCancelException(status);
    }

    this.status = CANCELLED;
  }
}
