package com.gurudev.aircnc.domain.room.repository;

import com.gurudev.aircnc.domain.room.entity.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

  Optional<Room> findByIdAndHostId(Long id, Long hostId);

  Optional<Room> findById(Long id);

  void deleteById(Long id);

  @Query("select r "
      + "from Room r "
      + "join fetch r.roomPhotos "
      + "join fetch r.host")
  Optional<Room> findByIdFetchRoomPhotosAndHost(Long id);
}
