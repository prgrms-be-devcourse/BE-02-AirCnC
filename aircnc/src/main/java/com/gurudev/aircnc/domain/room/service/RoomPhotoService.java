package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.io.InputStream;

public interface RoomPhotoService {

  RoomPhoto upload(InputStream in, long length, String key, String contentType);

}
