package com.gurudev.aircnc.exception;

import com.gurudev.aircnc.domain.trip.entity.TripStatus;

public class TripCancelException extends
    AircncRuntimeException {

  public TripCancelException(TripStatus tripStatus) {
    super("%s 상태의 여행은 취소 할 수 없습니다".formatted(tripStatus.getStatus()));
  }
}
