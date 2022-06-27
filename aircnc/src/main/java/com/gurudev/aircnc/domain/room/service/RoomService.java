package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomRegisterCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomUpdateCommand;
import java.util.List;

public interface RoomService {

  Room register(RoomRegisterCommand roomRegisterCommand);

  List<Room> getAll();

  Room update(RoomUpdateCommand roomUpdateCommand);
}
