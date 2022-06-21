package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import org.springframework.web.multipart.MultipartFile;

public interface RoomPhotoService {

  RoomPhoto upload(MultipartFile multipartFile);

}
