package com.gurudev.aircnc.controller.dto;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = PRIVATE)
public final class RoomDto {

  /**
   * form-data 바인딩을 위해 게터, 세터, 기본생성자 추가
   */
  @Getter
  @Setter
  @NoArgsConstructor
  public static class RoomRegisterRequest {

    private String name;
    private String lotAddress;
    private String roadAddress;
    private String detailedAddress;
    private String postCode;
    private String description;
    private int pricePerDay;
    private int capacity;

    public RoomRegisterRequest(String name, String lotAddress,
        String roadAddress, String detailedAddress, String postCode,
        String description, int pricePerDay, int capacity) {

      this.name = name;
      this.lotAddress = lotAddress;
      this.roadAddress = roadAddress;
      this.detailedAddress = detailedAddress;
      this.postCode = postCode;
      this.description = description;
      this.pricePerDay = pricePerDay;
      this.capacity = capacity;
    }
  }

  @Getter
  @RequiredArgsConstructor(access = PRIVATE)
  public static class RoomRegisterResponse {

    @JsonProperty("room")
    private final Response response;

    public static RoomRegisterResponse of(Room room) {
      return new RoomRegisterResponse(Response.of(room));
    }

    @Getter
    public static class Response {

      private final long id;
      private final String name;
      private final String address;
      private final String description;
      private final int pricePerDay;
      private final int capacity;
      private final List<String> fileNames;

      @Builder
      public Response(long id, String name, String address, String description, int pricePerDay,
          int capacity, List<String> fileNames) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.capacity = capacity;
        this.fileNames = fileNames;
      }

      public static Response of(Room room) {
        return Response.builder()
            .id(room.getId())
            .name(room.getName())
            .address(Address.toString(room.getAddress()))
            .description(room.getDescription())
            .pricePerDay(room.getPricePerDay())
            .capacity(room.getCapacity())
            .fileNames(room.getRoomPhotos().stream().map(RoomPhoto::getFileName)
                .collect(Collectors.toList()))
            .build();
      }
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class RoomUpdateRequest {

    private String name;
    private String description;
    private Integer pricePerDay;

    public RoomUpdateRequest(String name, String description, Integer pricePerDay) {
      this.name = name;
      this.description = description;
      this.pricePerDay = pricePerDay;
    }
  }

  @Getter
  @RequiredArgsConstructor(access = PRIVATE)
  public static class RoomUpdateResponse {

    @JsonProperty("room")
    private final Response response;

    public static RoomUpdateResponse of(Room room) {
      return new RoomUpdateResponse(Response.of(room));
    }

    @Getter
    public static class Response {

      private final long id;
      private final String name;
      private final String address;
      private final String description;
      private final int pricePerDay;
      private final int capacity;
      private final List<String> fileNames;

      @Builder
      public Response(long id, String name, String address, String description, int pricePerDay,
          int capacity, List<String> fileNames) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.capacity = capacity;
        this.fileNames = fileNames;
      }

      public static Response of(Room room) {
        return Response.builder()
            .id(room.getId())
            .name(room.getName())
            .address(Address.toString(room.getAddress()))
            .description(room.getDescription())
            .pricePerDay(room.getPricePerDay())
            .capacity(room.getCapacity())
            .fileNames(room.getRoomPhotos().stream().map(RoomPhoto::getFileName)
                .collect(Collectors.toList()))
            .build();
      }
    }
  }
}
