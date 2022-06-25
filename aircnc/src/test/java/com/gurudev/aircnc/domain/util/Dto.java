package com.gurudev.aircnc.domain.util;

import static java.util.stream.Collectors.toList;

import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.service.cmd.RoomCommand.RoomCreateCommand;
import com.gurudev.aircnc.domain.room.service.cmd.RoomPhotoCommand.RoomPhotoCreateCommand;
import java.util.List;

public class Dto {

  public static RoomCreateCommand of(Room room) {
    Address address = room.getAddress();

    return new RoomCreateCommand(room.getName(),
        address.getLotAddress(),
        address.getRoadAddress(),
        address.getDetailedAddress(),
        address.getPostCode(),
        room.getDescription(),
        room.getPricePerDay(),
        room.getCapacity());
  }

  public static List<RoomPhotoCreateCommand> listOf(List<RoomPhoto> roomPhotos) {
    return roomPhotos.stream()
        .map(roomPhoto -> new RoomPhotoCreateCommand(roomPhoto.getFileName()) {
        })
        .collect(toList());
  }
}
