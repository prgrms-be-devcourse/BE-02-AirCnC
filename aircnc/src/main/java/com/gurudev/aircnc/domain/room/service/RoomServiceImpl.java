package com.gurudev.aircnc.domain.room.service;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.repository.RoomRepository;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomDeleteCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomRegisterCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomUpdateCommand;
import com.gurudev.aircnc.domain.trip.repository.TripRepository;
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
  private final TripRepository tripRepository;

  @Transactional
  @Override
  public Room register(RoomRegisterCommand roomRegisterCommand) {
    Room room = roomRegisterCommand.toEntity();

    Member host = findMemberById(roomRegisterCommand.getHostId());

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
    Room room = roomRepository.findById(roomDeleteCommand.getRoomId())
        .orElseThrow(() -> new NotFoundException(Room.class));

    Member host = findMemberById(roomDeleteCommand.getHostId());

    checkArgument(deletable(room, host), "숙소를 삭제 할 수 없습니다");

    roomRepository.deleteById(roomDeleteCommand.getRoomId());
  }


  @Override
  public Room getById(Long id) {
    return findById(id);
  }

  private Room findById(Long id) {
    return roomRepository.findById(id).orElseThrow(() -> new NotFoundException(Room.class));
  }

  private Member findMemberById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(Member.class));
  }

  /*
   * 여행중이거나 예약중인 여행이 없으면서
   * 자신의 숙소인 경우 삭제 가능
   */
  private boolean deletable(Room room, Member host) {
    return !tripRepository.existsTravellingOrReservedByRoom(room) && room.isOwnedBy(host);
  }
}
