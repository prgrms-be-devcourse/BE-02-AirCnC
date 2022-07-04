package com.gurudev.aircnc.domain.room.service;

import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.TRAVELLING;
import static com.gurudev.aircnc.exception.Preconditions.checkArgument;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.repository.RoomRepository;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomDeleteCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomRegisterCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomUpdateCommand;
import com.gurudev.aircnc.domain.trip.repository.TripRepository;
import com.gurudev.aircnc.exception.NotFoundException;
import com.gurudev.aircnc.infrastructure.mail.entity.MailType;
import com.gurudev.aircnc.infrastructure.mail.service.EmailService;
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

  private final EmailService roomEmailService;

  @Transactional
  @Override
  public Room register(RoomRegisterCommand roomRegisterCommand) {
    Room room = roomRegisterCommand.toEntity();

    Member host = memberRepository.findById(roomRegisterCommand.getHostId())
        .orElseThrow(() -> new NotFoundException(Member.class));

    room.assignHost(host);
    roomEmailService.send(Email.toString(host.getEmail()), room.toMap(), MailType.REGISTER);
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

    Member host = room.getHost();

    roomEmailService.send(Email.toString(host.getEmail()), room.toMap(), MailType.UPDATE);
    return room.update(roomUpdateCommand.getName(), roomUpdateCommand.getDescription(),
        roomUpdateCommand.getPricePerDay());
  }

  @Transactional
  @Override
  public void delete(RoomDeleteCommand roomDeleteCommand) {
    Room findRoom = roomRepository.findById(roomDeleteCommand.getRoomId())
        .orElseThrow(() -> new NotFoundException(Room.class));

    checkArgument(isDeletable(findRoom.getHost().getId(), roomDeleteCommand.getRoomId(),
        roomDeleteCommand.getHostId()), "숙소를 삭제 할 수 없습니다");

    Member host = findRoom.getHost();
    roomEmailService.send(Email.toString(host.getEmail()), findRoom.toMap(), MailType.DELETE);
    roomRepository.deleteById(roomDeleteCommand.getRoomId());
  }

  // 이미 예약, 진행중인 trip이 있는 경우 삭제 불가 로직
  // TODO : 쿼리 최적화
  private boolean isDeletable(Long findRoomHostId, Long roomId, Long hostId) {
    if (!findRoomHostId.equals(hostId)) {
      return false;
    }

    PageRequest limitOne = PageRequest.of(0, 1);
    boolean containReservation = tripRepository.findByRoomIdAndStatusSet(
        roomId, Set.of(RESERVED, TRAVELLING), limitOne).size() == 1;

    return !containReservation;
  }

  @Override
  public Room getById(Long id) {
    return findById(id);
  }

  // TODO: 예약 불가능한 날짜 반환
  @Override
  public Room getDetailById(Long id) {
    return roomRepository.findByIdFetchRoomPhotosAndHost(id)
        .orElseThrow(() -> new NotFoundException(Room.class));
  }

  private Room findById(Long id) {
    return roomRepository.findById(id).orElseThrow(() -> new NotFoundException(Room.class));
  }

}
