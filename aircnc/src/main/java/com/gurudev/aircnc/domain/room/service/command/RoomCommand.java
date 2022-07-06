package com.gurudev.aircnc.domain.room.service.command;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.util.StringUtils.hasText;

import com.gurudev.aircnc.controller.dto.RoomDto.RoomRegisterRequest;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class RoomCommand {

  public static final int ROOM_DESCRIPTION_MIN_LENGTH = 10;
  public static final int ROOM_PRICE_PER_DAY_MIN_VALUE = 10000;

  @Getter
  public static class RoomRegisterCommand {

    private final String name;
    private final Address address;
    private final String description;
    private final int pricePerDay;
    private final int capacity;
    private final Long hostId;
    private final List<RoomPhoto> roomPhotos;

    private RoomRegisterCommand(String name, String lotAddress, String roadAddress,
        String detailedAddress,
        String postCode, String description, int pricePerDay, int capacity,
        List<String> photoFileNames,
        Long memberId) {

      checkArgument(hasText(name), "이름은 공백이 될 수 없습니다");
      checkArgument(hasText(description), "설명은 공백이 될 수 없습니다");
      checkArgument(description.length() >= ROOM_DESCRIPTION_MIN_LENGTH,
          String.format("설명은 %d 자 이상이어야 합니다", ROOM_DESCRIPTION_MIN_LENGTH));
      checkArgument(pricePerDay >= ROOM_PRICE_PER_DAY_MIN_VALUE,
          String.format("가격은 %d원 이상이어야 합니다", ROOM_PRICE_PER_DAY_MIN_VALUE));
      checkArgument(capacity >= 1, "인원수는 한명 이상이어야 합니다");

      this.name = name;
      this.address = new Address(lotAddress, roadAddress, detailedAddress, postCode);
      this.description = description;
      this.pricePerDay = pricePerDay;
      this.capacity = capacity;
      this.roomPhotos = photoFileNames.stream().map(RoomPhoto::new).collect(toList());
      this.hostId = memberId;
    }

    public static RoomRegisterCommand of(RoomRegisterRequest registerRequest,
        List<RoomPhoto> photos, Long hostId) {

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
          roomPhotoFiles,
          hostId);
    }

    public Room toEntity() {
      return new Room(name, address, description, pricePerDay, capacity, roomPhotos);
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
      if (name != null) {
        checkArgument(hasText(name), "이름은 공백이 될 수 없습니다");
      }

      if (description != null) {
        checkArgument(hasText(description), "설명은 공백이 될 수 없습니다");
        checkArgument(description.length() >= ROOM_DESCRIPTION_MIN_LENGTH,
            String.format("설명은 %d 자 이상이어야 합니다", ROOM_DESCRIPTION_MIN_LENGTH));
      }

      if (pricePerDay != null) {
        checkArgument(pricePerDay >= ROOM_PRICE_PER_DAY_MIN_VALUE,
            String.format("가격은 %d원 이상이어야 합니다", ROOM_PRICE_PER_DAY_MIN_VALUE));
      }

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
