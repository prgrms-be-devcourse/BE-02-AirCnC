package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.repository.RoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

  private final RoomRepository roomRepository;

  @Transactional
  @Override
  public Room register(Room room, List<RoomPhoto> roomPhotos) {
    roomPhotos.forEach(room::addRoomPhoto);

    return roomRepository.save(room);
  }
}