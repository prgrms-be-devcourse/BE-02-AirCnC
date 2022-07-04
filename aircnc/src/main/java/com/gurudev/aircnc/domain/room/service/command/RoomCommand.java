package com.gurudev.aircnc.domain.room.service.command;

import static java.util.stream.Collectors.toList;

import com.gurudev.aircnc.controller.dto.RoomDto.RoomRegisterRequest;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoomCommand {

  public static class RoomRegisterCommand {

    private final String name;
    private final String lotAddress;
    private final String roadAddress;
    private final String detailedAddress;
    private final String postCode;
    private final String description;
    private final int pricePerDay;
    private final int capacity;
    private final Long hostId;
    private final List<String> photoFileNames = new ArrayList<>();

    private RoomRegisterCommand(String name, String lotAddress, String roadAddress,
        String detailedAddress,
        String postCode, String description, int pricePerDay, int capacity,
        List<String> photoFileNames,
        Long memberId) {

      this.name = name;
      this.lotAddress = lotAddress;
      this.roadAddress = roadAddress;
      this.detailedAddress = detailedAddress;
      this.postCode = postCode;
      this.description = description;
      this.pricePerDay = pricePerDay;
      this.capacity = capacity;
      this.photoFileNames.addAll(photoFileNames);
      this.hostId = memberId;
    }

    public static RoomRegisterCommand of(RoomRegisterRequest registerRequest,
        List<RoomPhoto> photos,
        Long hostId) {

      List<String> roomPhotoFiles = photos.stream()
          .map(RoomPhoto::getFileName)
          .collect(toList());

      return new RoomRegisterCommand(
          registerRequest.getName(),
          registerRequest.getLotAddress(),
          registerRequest.getRoadAddress(),
          registerRequest.getDetailedAddress(),
          registerRequest.getPostCode(),
          registerRequest.getDescription(),
          registerRequest.getPricePerDay(),
          registerRequest.getCapacity(),
          roomPhotoFiles, hostId);
    }

    public Room toEntity() {
      Room room = new Room(name,
          new Address(lotAddress, roadAddress, detailedAddress, postCode),
          description, pricePerDay, capacity);
      photoFileNames.forEach(filename -> room.addRoomPhoto(new RoomPhoto(filename)));
      return room;
    }

    public Long getHostId() {
      return hostId;
    }
  }

  @Getter
  public static class RoomUpdateCommand {

    private final Long hostId;
    private final Long roomId;
    private final String name;
    private final String description;
    private final Integer pricePerDay;

    public RoomUpdateCommand(Long hostId, Long roomId, String name, String description,
        Integer pricePerDay) {
      this.hostId = hostId;
      this.roomId = roomId;
      this.name = name;
      this.description = description;
      this.pricePerDay = pricePerDay;
    }
  }

  @Getter
  public static class RoomDeleteCommand {

    private final Long hostId;
    private final Long roomId;

    public RoomDeleteCommand(Long hostId, Long roomId) {
      this.hostId = hostId;
      this.roomId = roomId;
    }
  }
}
