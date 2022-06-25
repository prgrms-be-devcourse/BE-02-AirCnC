package com.gurudev.aircnc.domain.room.service.cmd;

import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomCommand {

  private final String name;
  private final String lotAddress;
  private final String roadAddress;
  private final String detailedAddress;
  private final String postCode;
  private final String description;
  private final int pricePerDay;
  private final int capacity;

  public Room toEntity() {
    return new Room(name,
        new Address(lotAddress, roadAddress, detailedAddress, postCode),
        description, pricePerDay, capacity);
  }
}
