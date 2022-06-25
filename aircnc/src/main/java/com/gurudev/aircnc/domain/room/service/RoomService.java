package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.service.cmd.RoomCommand;
import com.gurudev.aircnc.domain.room.service.cmd.RoomPhotoCommand;
import java.util.List;

public interface RoomService {

  Room register(RoomCommand room, List<RoomPhotoCommand> roomPhotos, Long hostId);

  List<Room> getAll();
}
