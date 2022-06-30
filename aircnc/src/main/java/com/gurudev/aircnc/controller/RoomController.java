package com.gurudev.aircnc.controller;

import static com.gurudev.aircnc.controller.ApiResponse.created;
import static com.gurudev.aircnc.controller.ApiResponse.noContent;
import static com.gurudev.aircnc.controller.ApiResponse.ok;

import com.gurudev.aircnc.controller.dto.RoomDto.RoomRegisterRequest;
import com.gurudev.aircnc.controller.dto.RoomDto.RoomRegisterResponse;
import com.gurudev.aircnc.controller.dto.RoomDto.RoomUpdateRequest;
import com.gurudev.aircnc.controller.dto.RoomDto.RoomUpdateResponse;
import com.gurudev.aircnc.domain.base.AttachedFile;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.service.RoomPhotoService;
import com.gurudev.aircnc.domain.room.service.RoomService;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomDeleteCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomRegisterCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomUpdateCommand;
import com.gurudev.aircnc.infrastructure.security.jwt.JwtAuthentication;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/hosts/rooms")
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;
  private final RoomPhotoService roomPhotoService;

  /* 숙소 등록 */
  @PostMapping
  public ResponseEntity<RoomRegisterResponse> registerRoom(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @ModelAttribute RoomRegisterRequest request,
      @RequestPart List<MultipartFile> roomPhotosFile) {

    //숙소의 사진 S3에 저장
    List<RoomPhoto> roomPhotos = roomPhotosFile.stream()
        .map(AttachedFile::new)
        .map(roomPhotoService::upload)
        .collect(Collectors.toList());

    //숙소, S3에 저장된 숙소 사진의 파일 이름을 DB에 저장
    Room room =
        roomService.register(RoomRegisterCommand.of(request, roomPhotos, authentication.id));

    return created(RoomRegisterResponse.of(room, roomPhotos));
  }

  /* 숙소 변경 */
  @PatchMapping("/{roomId}")
  public ResponseEntity<RoomUpdateResponse> updateRoom(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @RequestBody RoomUpdateRequest request,
      @PathVariable("roomId") Long roomId) {

    Room room = roomService.update(new RoomUpdateCommand(authentication.id, roomId,
        request.getName(), request.getDescription(), request.getPricePerDay()));

    return ok(RoomUpdateResponse.of(room, room.getRoomPhotos()));
  }

  /* 숙소 삭제 */
  @DeleteMapping("/{roomId}")
  public ResponseEntity<?> deleteRoom(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable("roomId") Long roomId) {

    roomService.delete(new RoomDeleteCommand(authentication.id, roomId));

    return noContent();
  }
}
