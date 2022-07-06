package com.gurudev.aircnc.domain.trip.service.command;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static java.time.LocalDate.now;
import static lombok.AccessLevel.PRIVATE;

import com.gurudev.aircnc.controller.dto.TripDto.TripReserveRequest;
import com.gurudev.aircnc.controller.dto.TripDto.TripReserveRequest.Request;
import com.gurudev.aircnc.infrastructure.event.TripEvent;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class TripCommand {

  @Getter
  public static class TripReserveCommand {

    public static final int TRIP_TOTAL_PRICE_MIN_VALUE = 10000;

    private Long guestId;
    private Long roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int headCount;
    private int totalPrice;

    public TripReserveCommand(Long guestId, Long roomId, LocalDate checkIn,
        LocalDate checkOut, int headCount, int totalPrice) {
      checkArgument(checkOut.isAfter(checkIn), "체크아웃은 체크인 이전이 될 수 없습니다");
      checkArgument(checkIn.isEqual(now()) || checkIn.isAfter(now()),
          "체크인 날짜는" + now() + " 이전이 될 수 없습니다.");

      checkArgument(totalPrice >= TRIP_TOTAL_PRICE_MIN_VALUE,
          String.format("총 가격은 %d원 미만이 될 수 없습니다", TRIP_TOTAL_PRICE_MIN_VALUE));

      checkArgument(headCount >= 1, "인원은 1명 이상이여야 합니다");

      this.guestId = guestId;
      this.roomId = roomId;
      this.checkIn = checkIn;
      this.checkOut = checkOut;
      this.headCount = headCount;
      this.totalPrice = totalPrice;
    }

    public static TripReserveCommand of(TripReserveRequest tripReserveRequest, Long guestId) {
      Request request = tripReserveRequest.getRequest();

      return new TripReserveCommand(
          guestId,
          request.getRoomId(),
          request.getCheckIn(),
          request.getCheckOut(),
          request.getHeadCount(),
          request.getTotalPrice()
      );
    }

    public TripEvent toTripEvent() {
      return new TripEvent(guestId, roomId, checkIn, checkOut, headCount, totalPrice);
    }

  }
}
