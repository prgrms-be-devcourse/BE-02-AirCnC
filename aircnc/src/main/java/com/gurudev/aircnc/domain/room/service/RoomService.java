package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.dto.RoomDto;
import com.gurudev.aircnc.domain.room.dto.RoomPhotoDto;
import com.gurudev.aircnc.domain.room.entity.Room;
import java.util.List;

public interface RoomService {

  Room register(RoomDto room, List<RoomPhotoDto> roomPhotos, Long hostId);

  List<Room> getAll();
}
