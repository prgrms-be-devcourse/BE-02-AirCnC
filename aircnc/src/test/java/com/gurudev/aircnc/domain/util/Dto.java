package com.gurudev.aircnc.domain.util;

import static java.util.stream.Collectors.toList;

import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.service.cmd.RoomCommand;
import com.gurudev.aircnc.domain.room.service.cmd.RoomPhotoCommand;
import java.util.List;

public class Dto {

  public static RoomCommand of(Room room) {
    Address address = room.getAddress();

    return new RoomCommand(room.getName(),
        address.getLotAddress(),
        address.getRoadAddress(),
        address.getDetailedAddress(),
        address.getPostCode(),
        room.getDescription(),
        room.getPricePerDay(),
        room.getCapacity());
  }

  public static List<RoomPhotoCommand> listOf(List<RoomPhoto> roomPhotos) {
    return roomPhotos.stream().map(roomPhoto -> new RoomPhotoCommand(roomPhoto.getFileName()))
        .collect(
            toList());
  }
}
