package com.gurudev.aircnc.infrastructure.mail.utils;

import static lombok.AccessLevel.PRIVATE;

import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.infrastructure.event.TripEvent;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class MapUtils {

  public static Map<String, Object> toMap(Room room) {
    return Map.of("name", room.getName(),
        "address", Address.toString(room.getAddress()),
        "description", room.getDescription(),
        "pricePerDay", room.getPricePerDay(),
        "capacity", room.getCapacity());
  }

  public static Map<String, Object> toMap(Trip trip) {
    return Map.of(
        "checkIn", trip.getCheckIn(),
        "checkOut", trip.getCheckOut(),
        "totalPrice", trip.getTotalPrice(),
        "headCount", trip.getHeadCount(),
        "status", trip.getStatus());
  }

  public static Map<String, Object> toMap(TripEvent tripEvent) {
    return Map.of(
        "checkIn", tripEvent.getCheckIn(),
        "checkOut", tripEvent.getCheckOut(),
        "totalPrice", tripEvent.getTotalPrice(),
        "headCount", tripEvent.getHeadCount(),
        "status", tripEvent.getStatus());
  }
}
