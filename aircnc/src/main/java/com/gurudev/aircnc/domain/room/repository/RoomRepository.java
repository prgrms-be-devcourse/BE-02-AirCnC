package com.gurudev.aircnc.domain.room.repository;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.room.entity.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

  Optional<Room> findByIdAndHostId(Long id, Long hostId);

  @Query("select distinct r "
      + "from Room r "
      + "join fetch r.roomPhotos "
      + "join fetch r.host")
  Optional<Room> findByIdFetchRoomPhotosAndHost(Long id);

  @Query("select distinct r "
      + "from Room r "
      + "join fetch r.roomPhotos "
      + "join fetch r.host "
      + "where r.host = :host")
  List<Room> findByHost(Member host);
}
