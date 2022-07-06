package com.gurudev.aircnc.controller;

import static com.gurudev.aircnc.controller.ApiResponse.ok;

import com.gurudev.aircnc.controller.dto.RoomDto.RoomDetailResponse;
import com.gurudev.aircnc.controller.dto.RoomDto.RoomResponseList;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.service.RoomService;
import com.gurudev.aircnc.domain.trip.service.TripService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;
  private final TripService tripService;

  /* 모든 숙소 조회 */
  @GetMapping
  public ResponseEntity<RoomResponseList> getAllRooms() {
    List<Room> rooms = roomService.getAll();

    return ok(RoomResponseList.of(rooms));
  }

  @GetMapping("/{roomId}")
  public ResponseEntity<RoomDetailResponse> getDetailRoom(
      @PathVariable("roomId") Long roomId) {
    List<LocalDate> reservedDays = tripService.getReservedDaysById(roomId);
    Room room = roomService.getById(roomId);
    return ok(RoomDetailResponse.of(room, reservedDays));
  }
}
