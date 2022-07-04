package com.gurudev.aircnc.infrastructure.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 이벤트 객체를 publishing 해 주는 역할
 */
@Component
@RequiredArgsConstructor
public class EventPublisher {

  private final ApplicationEventPublisher publisher;

  public void publishTripEvent(TripEvent tripEvent) {
    publisher.publishEvent(tripEvent);
  }

}
