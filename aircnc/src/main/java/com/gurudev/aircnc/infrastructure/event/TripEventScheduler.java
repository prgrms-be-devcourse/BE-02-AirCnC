package com.gurudev.aircnc.infrastructure.event;

import com.gurudev.aircnc.domain.member.service.MemberService;
import com.gurudev.aircnc.domain.trip.service.TripService;
import com.gurudev.aircnc.infrastructure.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 큐를 풀링할 스케줄러
 */
@Component
@RequiredArgsConstructor
public class TripEventScheduler {

  private final TripEventQueue eventQueue;
  private final TripService tripService;
  private final MemberService memberService;
  private final EmailService tripEmailService;

  @Async("taskScheduler")
  @Scheduled(fixedRate = 100)
  public void tripReserveSchedule() {
    new TripEventRunner(eventQueue, tripService, memberService, tripEmailService)
        .run();
  }

}
