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
  public static class TripEvent {

    private Long guestId;
    private Long roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int headCount;
    private int totalPrice;
    private EventStatus status = EventStatus.STAND_BY;

    public TripEvent(Long guestId, Long roomId, LocalDate checkIn,
        LocalDate checkOut, int headCount, int totalPrice) {
      this.guestId = guestId;
      this.roomId = roomId;
      this.checkIn = checkIn;
      this.checkOut = checkOut;
      this.headCount = headCount;
      this.totalPrice = totalPrice;
    }

    public static TripEvent of(TripReserveRequest tripReserveRequest, Long guestId) {
      Request request = tripReserveRequest.getRequest();

      return new TripEvent(
          guestId,
          request.getRoomId(),
          request.getCheckIn(),
          request.getCheckOut(),
          request.getHeadCount(),
          request.getTotalPrice()
      );
    }

    public boolean isStandBy() {
      return this.status == EventStatus.STAND_BY;
    }

    public boolean isQueueWait() {
      return this.status == EventStatus.QUEUE_WAIT;
    }

    public void updateStatus(EventStatus status) {
      this.status = status;
    }

    public static enum EventStatus {
      STAND_BY, QUEUE, QUEUE_WAIT
    }

  }
}
