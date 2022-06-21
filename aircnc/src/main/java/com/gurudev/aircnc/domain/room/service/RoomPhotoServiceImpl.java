package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomPhotoServiceImpl implements RoomPhotoService {

  @Override
  public RoomPhoto upload(InputStream in, long length, String key, String contentType){
    //TODO - 사진 저장 로직

    return new RoomPhoto("대충 저장 경로");
  }
}
