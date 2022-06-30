package com.gurudev.aircnc.domain.trip.service.command;

import static lombok.AccessLevel.PRIVATE;

import com.gurudev.aircnc.controller.dto.TripDto.TripReserveRequest;
import com.gurudev.aircnc.controller.dto.TripDto.TripReserveRequest.Request;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class TripCommand {

  @Getter
  public static class TripReserveCommand {

    private Long guestId;
    private Long roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int headCount;
    private int totalPrice;

    public TripReserveCommand(Long guestId, Long roomId, LocalDate checkIn,
        LocalDate checkOut, int headCount, int totalPrice) {
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
  }
}
