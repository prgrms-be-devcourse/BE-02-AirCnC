package com.gurudev.aircnc.controller;

import static com.gurudev.aircnc.controller.dto.RoomDto.RoomRegisterResponse.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;

import com.gurudev.aircnc.configuration.jwt.JwtAuthentication;
import com.gurudev.aircnc.controller.dto.RoomDto.RoomRegisterRequest;
import com.gurudev.aircnc.controller.dto.RoomDto.RoomRegisterResponse;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.service.RoomPhotoService;
import com.gurudev.aircnc.domain.room.service.RoomService;
import com.gurudev.aircnc.domain.room.service.cmd.RoomPhotoCommand.RoomPhotoCreateCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;
  private final RoomPhotoService roomPhotoService;

  @PostMapping
  public ResponseEntity<RoomRegisterResponse> registerRoom(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @ModelAttribute RoomRegisterRequest roomDto,
      @RequestPart List<MultipartFile> roomPhotosFile) {

    List<RoomPhoto> roomPhotos = roomPhotosFile.stream()
        .map(roomPhotoService::upload)
        .collect(toList());

    List<RoomPhotoCreateCommand> roomPhotoCmds = roomPhotos.stream()
        .map(RoomPhotoCreateCommand::of)
        .collect(toList());

    Room room = roomService.register(roomDto.toDto(), roomPhotoCmds, authentication.id);

    return new ResponseEntity<>(of(room, roomPhotos), CREATED);
  }

}
