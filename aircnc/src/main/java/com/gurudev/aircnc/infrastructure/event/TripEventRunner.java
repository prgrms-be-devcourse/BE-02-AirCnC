package com.gurudev.aircnc.infrastructure.event;

import static com.gurudev.aircnc.domain.utils.MapUtils.toMap;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.service.MemberService;
import com.gurudev.aircnc.domain.trip.service.TripService;
import com.gurudev.aircnc.infrastructure.mail.entity.MailType;
import com.gurudev.aircnc.infrastructure.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Trip Event를 처리할 작업
 */
@Slf4j
@RequiredArgsConstructor
public class TripEventRunner implements Runnable {

  private final TripEventQueue eventQueue;
  private final TripService tripService;
  private final MemberService memberService;
  private final EmailService tripEmailService;


  @Override
  public void run() {
    if (eventQueue.isRemaining()) {
      TripEvent tripEvent = eventQueue.poll();
      try {
        tripService.reserve(tripEvent);
        handleSuccess(tripEvent);
      } catch (Exception e) {
        handleFailure(tripEvent);
      }
    }
  }

  private void handleSuccess(TripEvent tripEvent) {
    log.info("여행이 예약되었습니다! ");

    Member member = memberService.getById(tripEvent.getGuestId());
    tripEmailService.send(Email.toString(member.getEmail()), toMap(tripEvent), MailType.REGISTER);
  }

  private void handleFailure(TripEvent tripEvent) {
    log.info("여행 예약이 실패하였습니다! ");

    Member member = memberService.getById(tripEvent.getGuestId());
    tripEmailService.send(Email.toString(member.getEmail()), toMap(tripEvent), MailType.FAIL);
  }
}
