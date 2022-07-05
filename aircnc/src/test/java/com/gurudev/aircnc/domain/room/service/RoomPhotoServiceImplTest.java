package com.gurudev.aircnc.domain.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gurudev.aircnc.domain.base.AttachedFile;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.infrastructure.aws.s3.S3Client;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class RoomPhotoServiceImplTest {

  AmazonS3 mockAmazonS3;
  S3Client s3Client;
  RoomPhotoService roomPhotoService;

  @BeforeEach
  void setUp() {
    mockAmazonS3 = mock(AmazonS3.class);
    s3Client = new S3Client(mockAmazonS3, "url", "bucketName");
    roomPhotoService = new RoomPhotoServiceImpl(s3Client);
  }

  @Test
  void 파일_업로드_테스트() throws IOException {
    //given
    InputStream requestInputStream =
        new FileInputStream("src/test/resources/room-photos-src/photo1.jpeg");
    MockMultipartFile requestImage =
        new MockMultipartFile("roomPhotosFile", "photo1.jpeg", IMAGE_JPEG_VALUE,
            requestInputStream);

    AttachedFile attachedFile = new AttachedFile(requestImage);

    //when
    RoomPhoto roomPhoto = roomPhotoService.upload(attachedFile);

    //then
    assertThat(roomPhoto.getFileName()).isNotNull();
    verify(mockAmazonS3).putObject(any(PutObjectRequest.class));
  }
}