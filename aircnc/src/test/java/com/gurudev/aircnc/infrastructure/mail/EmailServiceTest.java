package com.gurudev.aircnc.infrastructure.mail;

import static com.gurudev.aircnc.domain.utils.MapUtils.toMap;

import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.util.Fixture;
import com.gurudev.aircnc.infrastructure.mail.entity.MailType;
import com.gurudev.aircnc.infrastructure.mail.service.EmailService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Disabled
class EmailServiceTest { // 사용방법을 위한 Temporary 테스트


  @Autowired
  @Qualifier("memberEmailService")
  private EmailService memberEmailService;

  @Autowired
  @Qualifier("roomEmailService")
  private EmailService roomEmailService;

  @Autowired
  @Qualifier("tripEmailService")
  private EmailService tripEmailService;


  private Room room;

  private Trip trip;

  @BeforeEach
  void setup() {
    room = Fixture.createRoom();

    trip = Fixture.createTrip();
  }

  @Test
  void 이메일_인증_전송_테스트() throws InterruptedException {
    memberEmailService.send("rlfrmsdh1@gmail.com", Collections.emptyMap(), MailType.REGISTER);
    Thread.sleep(5000);
  }

  @Test
  void 숙소_삭제_이메일_전송_테스트() throws InterruptedException {
    roomEmailService.send("rlfrmsdh1@gmail.com",
        toMap(room), MailType.DELETE);
    Thread.sleep(5000);
  }

  @Test
  void 숙소_등록_이메일_전송_테스트() throws InterruptedException {
    roomEmailService.send("rlfrmsdh1@gmail.com",
        toMap(room), MailType.REGISTER);
    Thread.sleep(5000);
  }

  @Test
  void 숙소_변경_이메일_전송_테스트() throws InterruptedException {
    roomEmailService.send("rlfrmsdh1@gmail.com",
        toMap(room), MailType.UPDATE);
    Thread.sleep(5000);
  }

  @Test
  void 여행_등록_이메일_전송_테스트() throws InterruptedException {
    tripEmailService.send("rlfrmsdh1@gmail.com",
        toMap(trip), MailType.REGISTER);
    Thread.sleep(5000);
  }

  @Test
  void 여행_취소_이메일_전송_테스트() throws InterruptedException {
    tripEmailService.send("rlfrmsdh1@gmail.com",
        toMap(trip), MailType.DELETE);
    Thread.sleep(5000);
  }

  @Test
  void 여행_등록_실패_이메일_전송_테스트() throws InterruptedException {
    tripEmailService.send("rlfrmsdh1@gmail.com",
        toMap(trip), MailType.FAIL);
    Thread.sleep(5000);
  }
}

