package com.gurudev.aircnc.domain.trip.entity;

import static com.gurudev.aircnc.domain.trip.entity.TripStatus.CANCELLED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static java.time.Period.between;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.exception.TripCancelException;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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

  @ManyToOne(fetch = LAZY)
  private Member guest;

  @ManyToOne(fetch = LAZY)
  private Room room;

  private LocalDate checkIn;

  private LocalDate checkOut;

  private int totalPrice;

  private int headCount;

  @Enumerated(STRING)
  private TripStatus status;

  public Trip(Member guest, Room room, LocalDate checkIn, LocalDate checkOut,
      int totalPrice, int headCount) {

    checkTotalPrice(checkIn, checkOut, totalPrice, room);
    checkHeadCount(headCount, room);

    this.guest = guest;
    this.room = room;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
    this.totalPrice = totalPrice;
    this.headCount = headCount;
    this.status = RESERVED;
  }

  private void checkHeadCount(int headCount, Room room) {
    checkArgument(headCount <= room.getCapacity(), "인원 수가 유효하지 않습니다");
  }

  /**
   * 서버의 총 가격과 요청 총 가격 검증
   */
  private void checkTotalPrice(LocalDate checkIn, LocalDate checkOut,
      int requestTotalPrice, Room room) {
    int calculatedTotalPrice = getDays(checkIn, checkOut) * room.getPricePerDay();
    checkArgument(requestTotalPrice == calculatedTotalPrice, "총 가격이 유효하지 않습니다");
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
