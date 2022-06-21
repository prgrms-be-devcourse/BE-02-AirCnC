package com.gurudev.aircnc.domain.room.service;

import static org.apache.commons.io.IOUtils.contentEquals;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class LocalRoomPhotoServiceImplTest {

  @Autowired
  private RoomPhotoService roomPhotoService;

  @Value("${room-photos.path}")
  private String roomPhotosPath;

  MockMultipartFile image1;

  @BeforeEach
  void setUp() throws IOException {
    String srcPath = "src/test/resources/room-photos-src/";
    FileInputStream fis1 =  new FileInputStream(srcPath + "photo1.jpeg");

    image1 = new MockMultipartFile("photo1", "photo1.jpeg", MediaType.IMAGE_JPEG_VALUE, fis1);
  }

  @Test
  void 로컬_숙소_사진_등록_성공() throws IOException {
    RoomPhoto uploadedRoomPhoto = roomPhotoService.upload(image1);
    String fileName = uploadedRoomPhoto.getPhotoUrl();

    InputStream uploadedInputStream = new FileInputStream(roomPhotosPath + fileName);
    InputStream requestInputStream = image1.getInputStream();

    assertThat(contentEquals(uploadedInputStream, requestInputStream)).isTrue();
  }

}