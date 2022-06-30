package com.gurudev.aircnc.controller.dto;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripEvent;
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
  public static class TripCancelResponse {

    @JsonProperty("trip")
    private final Response response;

    public static TripCancelResponse of(Trip trip) {
      return new TripCancelResponse(Response.of(trip));
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


  @Getter
  @RequiredArgsConstructor(access = PRIVATE)
  public static class TripDetailedResponse {

    @JsonProperty("trip")
    private final DetailedResponse response;

    public static TripDetailedResponse of(Trip trip) {
      return new TripDetailedResponse(DetailedResponse.of(trip));
    }

    @Getter
    public static class DetailedResponse {

      private final long id;
      private final LocalDate checkIn;
      private final LocalDate checkOut;
      private final int totalPrice;
      private final int headCount;
      private final String status;
      @JsonProperty("room")
      private final RoomResponse roomResponse;

      @Builder(access = PRIVATE)
      private DetailedResponse(long id, LocalDate checkIn, LocalDate checkOut, int totalPrice,
          int headCount, String status, Room room) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.headCount = headCount;
        this.status = status;
        this.roomResponse = RoomResponse.of(room);
      }

      public static DetailedResponse of(Trip trip) {
        return DetailedResponse.builder()
            .id(trip.getId())
            .checkIn(trip.getCheckIn())
            .checkOut(trip.getCheckOut())
            .totalPrice(trip.getTotalPrice())
            .headCount(trip.getHeadCount())
            .status(trip.getStatus().name())
            .room(trip.getRoom())
            .build();
      }

      @Getter
      public static class RoomResponse {

        private final long id;
        private final String name;
        private final String address;
        private final String hostName;
        private final List<String> fileNames;

        @Builder
        private RoomResponse(long id, String name, String address, String hostName,
            List<String> fileNames) {
          this.id = id;
          this.name = name;
          this.address = address;
          this.hostName = hostName;
          this.fileNames = fileNames;
        }

        public static RoomResponse of(Room room) {
          return RoomResponse.builder()
              .id(room.getId())
              .name(room.getName())
              .address(Address.toString(room.getAddress()))
              .hostName(room.getHost().getName())
              .fileNames(
                  room.getRoomPhotos().stream().map(RoomPhoto::getFileName).collect(toList()))
              .build();
        }
      }
    }
  }


  @Getter
  @RequiredArgsConstructor(access = PRIVATE)
  public static class TripReserveResponse {

    @JsonProperty("trip")
    private final Response response;

    public static TripReserveResponse of(TripEvent tripEvent) {
      return new TripReserveResponse(Response.of(tripEvent));
    }

    @Getter
    public static class Response {

      private final long guestId;
      private final long roomId;
      private final LocalDate checkIn;
      private final LocalDate checkOut;
      private final int totalPrice;
      private final int headCount;

      @Builder
      private Response(long guestId, long roomId,
          LocalDate checkIn, LocalDate checkOut, int totalPrice, int headCount) {
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.headCount = headCount;
      }

      public static Response of(TripEvent tripEvent) {
        return Response.builder()
            .guestId(tripEvent.getGuestId())
            .roomId(tripEvent.getRoomId())
            .checkIn(tripEvent.getCheckIn())
            .checkOut(tripEvent.getCheckOut())
            .totalPrice(tripEvent.getTotalPrice())
            .headCount(tripEvent.getHeadCount())
            .build();
      }
    }
  }


}
