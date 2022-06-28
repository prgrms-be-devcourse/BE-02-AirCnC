package com.gurudev.aircnc.controller.dto;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class TripDto {

  @Getter
  public static class TripReserveRequest {

    @JsonProperty("trip")
    private Request request;

    @Getter
    public static class Request {

      private LocalDate checkIn;
      private LocalDate checkOut;
      private int totalPrice;
      private int headCount;
      private long roomId;
    }
  }

  @Getter
  @RequiredArgsConstructor(access = PRIVATE)
  public static class TripResponse {

    @JsonProperty("trip")
    private final Response response;

    public static TripResponse of(Trip trip) {
      return new TripResponse(Response.of(trip));
    }
  }

  @Getter
  @RequiredArgsConstructor(access = PRIVATE)
  public static class TripResponseList {

    @JsonProperty("trips")
    private final List<Response> response;

    public static TripResponseList of(List<Trip> trips) {
      return new TripResponseList(trips.stream().map(Response::of).collect(toList()));
    }
  }


  @Getter
  public static class Response {

    private final long id;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final int totalPrice;
    private final int headCount;
    private final long roomId;
    private final String status;

    @Builder(access = PRIVATE)
    private Response(long id, LocalDate checkIn, LocalDate checkOut, int totalPrice,
        int headCount,
        long roomId, String status) {
      this.id = id;
      this.checkIn = checkIn;
      this.checkOut = checkOut;
      this.totalPrice = totalPrice;
      this.headCount = headCount;
      this.roomId = roomId;
      this.status = status;
    }

    public static Response of(Trip trip) {
      return Response.builder()
          .id(trip.getId())
          .checkIn(trip.getCheckIn())
          .checkOut(trip.getCheckOut())
          .totalPrice(trip.getTotalPrice())
          .headCount(trip.getHeadCount())
          .roomId(trip.getRoom().getId())
          .status(trip.getStatus().name())
          .build();
    }
  }

}
