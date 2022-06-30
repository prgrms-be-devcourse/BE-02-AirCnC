package com.gurudev.aircnc.infrastructure.event;

import static com.gurudev.aircnc.exception.Preconditions.checkState;

import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripEvent;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 예약 이벤트가 대기할 큐
 */
public class TripEventQueue {

  private final Queue<TripEvent> queue;
  private final int queueSize;

  public TripEventQueue(int queueSize) {
    this.queueSize = queueSize;
    this.queue = new LinkedBlockingQueue<>(queueSize);
  }

  public boolean offer(TripEvent event) {
    return queue.offer(event);
  }

  public TripEvent poll() {
    checkState(queue.size() > 0, "큐에 이벤트가 존재하지 않습니다.");

    return queue.poll();
  }

  public boolean isFull() {
    return queue.size() == queueSize;
  }

  public boolean isRemaining() {
    return queue.size() > 0;
  }
}
