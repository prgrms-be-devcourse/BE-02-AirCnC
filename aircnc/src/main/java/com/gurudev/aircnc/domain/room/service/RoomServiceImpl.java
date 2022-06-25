package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.domain.room.dto.RoomDto;
import com.gurudev.aircnc.domain.room.dto.RoomPhotoDto;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.repository.RoomRepository;
import com.gurudev.aircnc.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

  private final MemberRepository memberRepository;
  private final RoomRepository roomRepository;

  @Transactional
  @Override
  public Room register(RoomDto roomDto, List<RoomPhotoDto> roomPhotoDtos, Long hostId) {
    Room room = roomDto.toEntity();
    List<RoomPhoto> roomPhotos = roomPhotoDtos.stream().map(RoomPhotoDto::toEntity).toList();

    roomPhotos.forEach(room::addRoomPhoto);

    Member host = memberRepository.findById(hostId)
        .orElseThrow(() -> new NotFoundException(Member.class));

    room.assignHost(host);

    return roomRepository.save(room);
  }

  @Override
  public List<Room> getAll() {
    return roomRepository.findAll();
  }
}
