package com.gurudev.aircnc.domain.util;

import com.gurudev.aircnc.controller.dto.RoomDto.RoomRegisterRequest;
import com.gurudev.aircnc.domain.member.entity.Role;
import com.gurudev.aircnc.domain.member.service.command.MemberCommand.MemberRegisterCommand;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomDeleteCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomRegisterCommand;
import java.time.LocalDate;
import java.util.List;

public class Command {

  public static MemberRegisterCommand ofHost() {
    return new MemberRegisterCommand("host@haha.com",
        "paSSword!",
        "ndy",
        LocalDate.of(1997, 8, 21),
        "010-1234-5678",
        Role.HOST.name());
  }

  public static MemberRegisterCommand ofHost(String email) {
    return new MemberRegisterCommand(email,
        "paSSword!",
        "ndy",
        LocalDate.of(1997, 8, 21),
        "010-1234-5678",
        Role.HOST.name());
  }

  public static MemberRegisterCommand ofGuest() {
    return new MemberRegisterCommand("guest@haha.com",
        "paSSword!",
        "ndy",
        LocalDate.of(1997, 8, 21),
        "010-1234-5678",
        Role.GUEST.name());
  }

  public static RoomRegisterCommand ofRoom(Room room, List<RoomPhoto> roomPhotos,
      Long hostId) {
    Address address = room.getAddress();
    RoomRegisterRequest request = new RoomRegisterRequest(
        room.getName(), address.getLotAddress(),
        address.getRoadAddress(), address.getDetailedAddress(), address.getPostCode(),
        room.getDescription(), room.getPricePerDay(), room.getCapacity());

    return RoomRegisterCommand.of(request, roomPhotos, hostId);
  }

  public static RoomDeleteCommand ofHost(Long hostId, Long roomId) {
    return new RoomDeleteCommand(hostId, roomId);
  }
}
