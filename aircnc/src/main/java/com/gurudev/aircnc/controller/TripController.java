package com.gurudev.aircnc.controller;


import static com.gurudev.aircnc.controller.dto.TripDto.TripInfoResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.gurudev.aircnc.controller.dto.TripDto.TripDetailedResponse;
import com.gurudev.aircnc.controller.dto.TripDto.TripReserveRequest;
import com.gurudev.aircnc.controller.dto.TripDto.TripReserveRequest.Request;
import com.gurudev.aircnc.controller.dto.TripDto.TripResponse;
import com.gurudev.aircnc.controller.dto.TripDto.TripResponseList;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.service.ReserveService;
import com.gurudev.aircnc.domain.trip.service.TripService;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripEvent;
import com.gurudev.aircnc.infrastructure.security.jwt.JwtAuthentication;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {

  private final TripService tripService;
  private final ReserveService reserveService;

  /* 여행 예약 */
  @PostMapping
  public ResponseEntity<TripInfoResponse> tripReserve(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @RequestBody TripReserveRequest tripReserveRequest) {

    Request request = tripReserveRequest.getRequest();

    TripEvent tripEvent =
        new TripEvent(
            authentication.id,
            request.getRoomId(),
            request.getCheckIn(),
            request.getCheckOut(),
            request.getHeadCount(),
            request.getTotalPrice()
        );

    TripEvent reserveTripInfo = reserveService.reserve(tripEvent);

    return new ResponseEntity<>(TripInfoResponse.of(reserveTripInfo), CREATED);
  }

  /* 여행 목록 조회 */
  @GetMapping
  public ResponseEntity<TripResponseList> getAllTrip(
      @AuthenticationPrincipal JwtAuthentication authentication) {

    List<Trip> trips = tripService.getByGuestId(authentication.id);

    return new ResponseEntity<>(TripResponseList.of(trips), OK);
  }

  /* 여행 상세 조회 */
  @GetMapping("/{tripId}")
  public ResponseEntity<TripDetailedResponse> getDetailedTrip(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable Long tripId) {

    Trip trip = tripService.getDetailedById(tripId, authentication.id);

    return new ResponseEntity<>(TripDetailedResponse.of(trip), OK);
  }

  /* 여행 취소 */
  @PostMapping("/{tripId}/cancel")
  public ResponseEntity<TripResponse> cancelTrip(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable Long tripId) {

    Trip trip = tripService.cancel(tripId, authentication.id);

    return new ResponseEntity<>(TripResponse.of(trip), OK);
  }
}
