package com.gurudev.aircnc.infrastructure.event;

import com.gurudev.aircnc.domain.trip.service.TripService;
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

  @Override
  public void run() {
    if (eventQueue.isRemaining()) {
      TripEvent tripEvent = eventQueue.poll();
      try {
        tripService.reserve(tripEvent);
        handlingInCaseOfSuccess(tripEvent);
      } catch (Exception e) {
        handlingInCaseOfFailure(tripEvent);
      }
    }
  }

  private void handlingInCaseOfSuccess(TripEvent tripEvent) {
    log.info("여행이 예약되었습니다! ");
    // TODO 예약 성공 메일 보내기
  }

  private void handlingInCaseOfFailure(TripEvent tripEvent) {
    log.info("여행 예약이 실패하였습니다! ");
    // TODO 예약 실패 메일 보내기
  }
}
