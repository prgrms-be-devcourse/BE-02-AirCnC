package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.common.AttachedFile;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;

public interface RoomPhotoService {

  RoomPhoto upload(AttachedFile attachedFile);

}
