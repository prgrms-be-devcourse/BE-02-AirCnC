package com.gurudev.aircnc.infrastructure.event;

import static com.gurudev.aircnc.infrastructure.event.TripEvent.EventStatus.QUEUE;
import static com.gurudev.aircnc.infrastructure.event.TripEvent.EventStatus.QUEUE_WAIT;

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
