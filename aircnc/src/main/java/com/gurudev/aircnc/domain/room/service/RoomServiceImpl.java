package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.repository.RoomRepository;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomRegisterCommand;
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
}
