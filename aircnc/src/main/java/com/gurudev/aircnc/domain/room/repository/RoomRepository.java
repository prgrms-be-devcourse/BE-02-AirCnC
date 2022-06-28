package com.gurudev.aircnc.domain.room.repository;

import com.gurudev.aircnc.domain.room.entity.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

  Optional<Room> findByIdAndHostId(Long id, Long hostId);

  Optional<Room> findById(Long id);

  void deleteById(Long id);
}
