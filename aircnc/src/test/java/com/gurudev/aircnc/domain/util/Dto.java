package com.gurudev.aircnc.domain.util;

import com.gurudev.aircnc.domain.room.dto.RoomDto;
import com.gurudev.aircnc.domain.room.dto.RoomPhotoDto;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.util.List;

public class Dto {

  public static RoomDto of(Room room) {
    Address address = room.getAddress();

    return new RoomDto(room.getName(),
        address.getLotAddress(),
        address.getRoadAddress(),
        address.getDetailedAddress(),
        address.getPostCode(),
        room.getDescription(),
        room.getPricePerDay(),
        room.getCapacity());
  }

  public static List<RoomPhotoDto> listOf(List<RoomPhoto> roomPhotos) {
    return roomPhotos.stream().map(roomPhoto -> new RoomPhotoDto(roomPhoto.getFileName())).toList();
  }
}
