package com.gurudev.aircnc.controller.dto;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


public class RoomDto {

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

    public com.gurudev.aircnc.domain.room.dto.RoomDto toDto() {
      return new com.gurudev.aircnc.domain.room.dto.RoomDto(
          name, lotAddress, roadAddress, detailedAddress, postCode,
          description, pricePerDay, capacity);
    }
  }

  @Getter
  @RequiredArgsConstructor(access = PRIVATE)
  public static class RoomRegisterResponse {

    @JsonProperty("room")
    private final Response response;

    public static RoomRegisterResponse of(Room room, List<RoomPhoto> roomPhotos) {
      return new RoomRegisterResponse(Response.of(room, roomPhotos));
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
      public Response(long id, String name, String address, String roadAddress,
          String detailedAddress, String postCode, String description, int pricePerDay,
          int capacity,
          List<String> fileNames) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.capacity = capacity;
        this.fileNames = fileNames;
      }

      public static Response of(Room room, List<RoomPhoto> roomPhotos) {
        return Response.builder()
            .id(room.getId())
            .name(room.getName())
            .address(Address.toString(room.getAddress()))
            .description(room.getDescription())
            .pricePerDay(room.getPricePerDay())
            .capacity(room.getCapacity())
            .fileNames(roomPhotos.stream().map(RoomPhoto::getFileName).collect(Collectors.toList()))
            .build();
      }
    }
  }

  @Getter
  @RequiredArgsConstructor(access = PRIVATE)
  public static class RoomResponses {

    @JsonProperty("rooms")
    private final List<RoomInfo> roomInfos;

    public static RoomResponses of(List<Room> room) {
      List<RoomInfo> roomInfos = room.stream().map(r -> RoomInfo.of(r))
          .collect(Collectors.toList());
      return new RoomResponses(roomInfos);
    }

    @Getter
    public static class RoomInfo {

      private final long id;
      private final String address;
      private final int pricePerDay;
      private String photoUrl;
//      private final String wishListId;

      @Builder(access = PRIVATE)
      public RoomInfo(long id, String address, int pricePerDay, String photoUrl) {
        this.id = id;
        this.address = address;
        this.pricePerDay = pricePerDay;
        this.photoUrl = photoUrl;
      }

      public static RoomInfo of(Room room) {
        String photoUrl = room.getRoomPhotos().stream().findFirst().map(RoomPhoto::getFileName)
            .orElseThrow(() -> new NotFoundException(RoomPhoto.class));

        return RoomInfo.builder()
            .id(room.getId())
            .address(Address.toString(room.getAddress()))
            .pricePerDay(room.getPricePerDay())
            .photoUrl(photoUrl)
            .build();

      }
    }
  }
}

