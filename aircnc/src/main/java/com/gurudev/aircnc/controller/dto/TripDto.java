package com.gurudev.aircnc.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Getter;

public class TripDto {

  @Getter
  public static class TripReserveRequest {

    @JsonProperty("trip")
    private Request request;

    public LocalDate getCheckIn() {
      return request.checkIn;
    }

    public LocalDate getCheckOut() {
      return request.checkOut;
    }

    public int getTotalPrice() {
      return request.totalPrice;
    }

    public int getHeadCount() {
      return request.headCount;
    }

    public long getRoomId() {
      return request.roomId;
    }

    @Getter
    private static class Request {

      private LocalDate checkIn;
      private LocalDate checkOut;
      private int totalPrice;
      private int headCount;
      private long roomId;
    }
  }
}
