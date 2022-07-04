package com.gurudev.aircnc.domain.utils;

import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
}
