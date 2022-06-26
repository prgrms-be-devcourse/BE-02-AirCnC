package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.service.cmd.RoomCommand.RoomRegisterCommand;
import java.util.List;

public interface RoomService {

  Room register(RoomRegisterCommand roomRegisterCommand);

  List<Room> getAll();
}
