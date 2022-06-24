package com.gurudev.aircnc.domain.room.service;

import static org.apache.commons.io.IOUtils.contentEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class LocalRoomPhotoServiceTest {

  @Autowired
  private RoomPhotoService roomPhotoService;

  @Value("${room-photos.path}")
  private String roomPhotosPath;

  MockMultipartFile requestImage;

  @BeforeEach
  void setUp() throws IOException {
    InputStream requestInputStream =  new FileInputStream("src/test/resources/room-photos-src/photo1.jpeg");

    requestImage = new MockMultipartFile("photo1", "photo1.jpeg", IMAGE_JPEG_VALUE, requestInputStream);
  }

  @Test
  void 로컬_숙소_사진_등록_성공() throws IOException {
    RoomPhoto uploadedRoomPhoto = roomPhotoService.upload(requestImage);
    String fileName = uploadedRoomPhoto.getFileName();

    InputStream uploadedInputStream = new FileInputStream(roomPhotosPath + fileName);
    InputStream requestInputStream = requestImage.getInputStream();

    assertThat(contentEquals(uploadedInputStream, requestInputStream)).isTrue();
  }

}