package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface RoomPhotoService {

  List<RoomPhoto> upload(List<MultipartFile> multipartFiles);

  RoomPhoto upload(MultipartFile multipartFile);

}
