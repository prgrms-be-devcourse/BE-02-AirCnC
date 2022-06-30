package com.gurudev.aircnc.infrastructure.event;

import static com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripEvent.EventStatus.QUEUE;
import static com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripEvent.EventStatus.QUEUE_WAIT;

import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * TripEvent 이벤트 처리를 담당할 listener
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TripEventListener {

  /**
   * EventQueue에 작업을 집어넣는 Flow
   * <p>
   * 1. 만약 tripEvent가 STANTBY 상태가 아니라면 -> 잘못된 상태 넘어가~ 2  eventQueue가 꽉 차 있다면! 잠시 기다려 곧 넣어줄게(자리 날 때까지
   * 무한루프) 3. 비었지? 자 드가자~(Queue에)
   */
  private final TripEventQueue eventQueue;

  @EventListener
  public void onEvent(TripEvent tripEvent) {
    if (!tripEvent.isStandBy()) {
      return;
    }

    while (eventQueue.isFull()) {
      if (!tripEvent.isQueueWait()) {
        tripEvent.updateStatus(QUEUE_WAIT);
      }
    }

    tripEvent.updateStatus(QUEUE);
    eventQueue.offer(tripEvent);
  }


}
