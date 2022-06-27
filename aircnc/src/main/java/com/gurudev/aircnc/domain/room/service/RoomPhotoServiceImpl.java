package com.gurudev.aircnc.domain.room.service;

import static java.util.stream.Collectors.toList;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class RoomPhotoServiceImpl implements RoomPhotoService {

  @Transactional
  @Override
  public List<RoomPhoto> upload(List<MultipartFile> multipartFile) {
    return multipartFile.stream()
        .map(this::upload)
        .collect(toList());
  }

  @Transactional
  @Override
  public RoomPhoto upload(MultipartFile multipartFile) {
    //TODO
    return new RoomPhoto("TODO");
  }
}
