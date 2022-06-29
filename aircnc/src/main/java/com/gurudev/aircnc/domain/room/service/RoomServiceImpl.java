package com.gurudev.aircnc.domain.room.service;

import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.TRAVELLING;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.repository.RoomRepository;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomDeleteCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomRegisterCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomUpdateCommand;
import com.gurudev.aircnc.domain.trip.repository.TripRepository;
import com.gurudev.aircnc.exception.NotFoundException;
import com.gurudev.aircnc.exception.RoomDeleteException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

  private final MemberRepository memberRepository;
  private final RoomRepository roomRepository;
  private final TripRepository tripRepository;

  @Transactional
  @Override
  public Room register(RoomRegisterCommand roomRegisterCommand) {
    Room room = roomRegisterCommand.toEntity();

    Member host = memberRepository.findById(roomRegisterCommand.getHostId())
        .orElseThrow(() -> new NotFoundException(Member.class));

    room.assignHost(host);

    return roomRepository.save(room);
  }

  @Override
  public List<Room> getAll() {
    return roomRepository.findAll();
  }

  @Transactional
  @Override
  public Room update(RoomUpdateCommand roomUpdateCommand) {
    Room room = roomRepository
        .findByIdAndHostId(roomUpdateCommand.getRoomId(), roomUpdateCommand.getHostId())
        .orElseThrow(() -> new NotFoundException(Room.class));

    return room.update(roomUpdateCommand.getName(), roomUpdateCommand.getDescription(),
        roomUpdateCommand.getPricePerDay());
  }

  @Transactional
  @Override
  public void delete(RoomDeleteCommand roomDeleteCommand) {
    Room findRoom = roomRepository.findById(roomDeleteCommand.getRoomId())
        .orElseThrow(() -> new NotFoundException(Room.class));

    PageRequest limitOne = PageRequest.of(0, 1);
    if (tripRepository.findByRoomIdAndStatusSet(
        roomDeleteCommand.getRoomId(),
        Set.of(RESERVED, TRAVELLING), limitOne).size() == 1
        || !findRoom.getHost().getId().equals(roomDeleteCommand.getHostId())) {
      throw new RoomDeleteException("숙소를 삭제 할 수 없습니다");
    }

    roomRepository.deleteById(roomDeleteCommand.getRoomId());
  }

  @Override
  public Room getById(Long id) {
    return findById(id);
  }

  // TODO: 예약 가능한 날짜 반환
  @Override
  public Room getDetailById(Long id) {
    return roomRepository.findByIdFetchRoomPhotosAndHost(id)
        .orElseThrow(() -> new NotFoundException(Room.class));
  }

  private Room findById(Long id) {
    return roomRepository.findById(id).orElseThrow(() -> new NotFoundException(Room.class));
  }

}
