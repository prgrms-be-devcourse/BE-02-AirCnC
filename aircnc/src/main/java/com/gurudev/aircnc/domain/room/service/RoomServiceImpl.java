package com.gurudev.aircnc.domain.room.service;

import static com.gurudev.aircnc.exception.Preconditions.checkCondition;
import static com.gurudev.aircnc.infrastructure.mail.utils.MapUtils.toMap;

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

  private final EmailService roomEmailService;

  @Transactional
  @Override
  public Room register(RoomRegisterCommand roomRegisterCommand) {
    Room room = roomRegisterCommand.toEntity();
    roomRegisterCommand.getRoomPhotos().forEach(room::addRoomPhoto);

    Member host = findMemberById(roomRegisterCommand.getHostId());
    room.assignHost(host);

    roomEmailService.send(Email.toString(host.getEmail()), toMap(room), MailType.REGISTER);
    return roomRepository.save(room);
  }

  @Override
  public List<Room> getAll() {
    return roomRepository.findAll();
  }

  @Transactional
  @Override
  public Room update(RoomUpdateCommand roomUpdateCommand) {
    Room room = findById(roomUpdateCommand.getRoomId());
    Member host = findMemberById(roomUpdateCommand.getHostId());

    checkCondition(room.isOwnedBy(host), "숙소를 수정 할 수 없습니다");

    roomEmailService.send(Email.toString(host.getEmail()), toMap(room), MailType.UPDATE);
    return room.update(roomUpdateCommand.getName(),
        roomUpdateCommand.getDescription(),
        roomUpdateCommand.getPricePerDay());
  }

  @Transactional
  @Override
  public void delete(RoomDeleteCommand roomDeleteCommand) {
    Room room = findById(roomDeleteCommand.getRoomId());
    Member host = findMemberById(roomDeleteCommand.getHostId());

    checkCondition(isDeletable(room, host), "숙소를 삭제 할 수 없습니다");

    roomEmailService.send(Email.toString(host.getEmail()), toMap(room), MailType.DELETE);
    roomRepository.deleteById(roomDeleteCommand.getRoomId());
  }


  @Override
  public Room getById(Long id) {
    return findById(id);
  }

  @Override
  public Room getDetailById(Long id) {
    return roomRepository.findByIdFetchRoomPhotosAndHost(id)
        .orElseThrow(() -> new NotFoundException(Room.class));
  }

  @Override
  public List<Room> getByHostId(Long hostId) {
    Member host = memberRepository.findById(hostId)
        .orElseThrow(() -> new NotFoundException(Member.class));

    return roomRepository.findByHost(host);
  }

  private Room findById(Long id) {
    return roomRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(Room.class));
  }

  private Member findMemberById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(Member.class));
  }

  /*
   * 여행중이거나 예약중인 여행이 없으면서
   * 자신의 숙소인 경우 삭제 가능
   */
  private boolean isDeletable(Room room, Member host) {
    return !tripRepository.existsByTravellingOrReserved(room) && room.isOwnedBy(host);
  }
}
