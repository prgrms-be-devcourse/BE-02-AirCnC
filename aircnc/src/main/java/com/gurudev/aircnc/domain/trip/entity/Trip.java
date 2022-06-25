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

/**
 * 여행
 * <li> 총 가격은 (checkOut 날짜 - checkIn 날짜) * (숙소의 1박당 가격) 로 계산한다</li>
 * <li> 가격 기준은 원화(₩) </li>
 */
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
          int totalPrice, int headCount) {
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
    this.status = RESERVED;
  }

  private void checkHeadCount(int headCount, Room room) {
    if (room.getCapacity() < headCount) {
      throw new TripReservationException("인원 수가 유효하지 않습니다");
    }
  }

  /**
   * 서버의 총 가격과 요청 총 가격 검증
   */
  private void checkTotalPrice(LocalDate checkIn, LocalDate checkOut, int requestTotalPrice,
          Room room) {
    int calculatedTotalPrice = getDays(checkIn, checkOut) * room.getPricePerDay();
    if (requestTotalPrice != calculatedTotalPrice) {
      throw new TripReservationException("총 가격이 유효하지 않습니다");
    }
  }

  private int getDays(LocalDate from, LocalDate to) {
    return between(from, to).getDays();
  }

  /**
   * 예약 상태의 여행만 취소 할 수 있다
   */
  public void cancel() {
    if (status != RESERVED) {
      throw new TripCancelException(status);
    }

    this.status = CANCELLED;
  }

}
