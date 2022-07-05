package com.gurudev.aircnc.controller;

import static com.gurudev.aircnc.controller.ApiResponse.ok;

import com.gurudev.aircnc.controller.dto.RoomDto.RoomResponseList;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.service.RoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;

  @GetMapping
  public ResponseEntity<RoomResponseList> getAllRooms() {
    List<Room> rooms = roomService.getAll();

    return ok(RoomResponseList.of(rooms));
  }

}
