package com.gurudev.aircnc.domain.util;

import com.gurudev.aircnc.controller.dto.RoomDto.RoomRegisterRequest;
import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.service.command.MemberCommand.MemberRegisterCommand;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomDeleteCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomRegisterCommand;
import java.util.List;

public class Command {

  public static MemberRegisterCommand ofRegisterMember(Member member) {
    return new MemberRegisterCommand(Email.toString(member.getEmail()),
        Password.toString(member.getPassword()),
        member.getName(),
        member.getBirthDate(),
        PhoneNumber.toString(member.getPhoneNumber()),
        member.getRole().name());
  }

  public static RoomRegisterCommand ofRegisterRoom(Room room, List<RoomPhoto> roomPhotos,
      Long hostId) {
    Address address = room.getAddress();
    RoomRegisterRequest request = new RoomRegisterRequest(
        room.getName(), address.getLotAddress(),
        address.getRoadAddress(), address.getDetailedAddress(), address.getPostCode(),
        room.getDescription(), room.getPricePerDay(), room.getCapacity());

    return RoomRegisterCommand.of(request, roomPhotos, hostId);
  }

  public static RoomDeleteCommand ofDeleteRoom(Long hostId, Long roomId) {
    return new RoomDeleteCommand(hostId, roomId);
  }
}
