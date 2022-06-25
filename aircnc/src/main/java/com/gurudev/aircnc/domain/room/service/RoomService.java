package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.service.cmd.RoomCommand.RoomCreateCommand;
import com.gurudev.aircnc.domain.room.service.cmd.RoomPhotoCommand.RoomPhotoCreateCommand;
import java.util.List;

public interface RoomService {

  Room register(RoomCreateCommand room, List<RoomPhotoCreateCommand> roomPhotos, Long hostId);

  List<Room> getAll();
}
