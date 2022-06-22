package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.util.List;

public interface RoomService {

  Room register(Room room, List<RoomPhoto> roomPhotos);

}
