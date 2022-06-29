package com.gurudev.aircnc.controller;


import static com.gurudev.aircnc.controller.ApiResponse.created;
import static com.gurudev.aircnc.controller.ApiResponse.ok;

import com.gurudev.aircnc.controller.dto.TripDto.TripDetailedResponse;
import com.gurudev.aircnc.controller.dto.TripDto.TripReserveRequest;
import com.gurudev.aircnc.controller.dto.TripDto.TripReserveRequest.Request;
import com.gurudev.aircnc.controller.dto.TripDto.TripResponse;
import com.gurudev.aircnc.controller.dto.TripDto.TripResponseList;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.service.TripService;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand;
import com.gurudev.aircnc.infrastructure.mail.EmailService;
import com.gurudev.aircnc.infrastructure.mail.MailKind;
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

  private final EmailService tripEmailService;

  /* 여행 예약 */
  @PostMapping
  public ResponseEntity<TripResponse> tripReserve(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @RequestBody TripReserveRequest tripReserveRequest) {

    Request request = tripReserveRequest.getRequest();

    TripReserveCommand tripReserveCommand =
        new TripReserveCommand(
            authentication.id,
            request.getRoomId(),
            request.getCheckIn(),
            request.getCheckOut(),
            request.getHeadCount(),
            request.getTotalPrice()
        );

    Trip trip = tripService.reserve(tripReserveCommand);
    tripEmailService.send(authentication.email, trip.toMap(), MailKind.REGISTER);
    return created(TripResponse.of(trip));
  }

  /* 여행 목록 조회 */
  @GetMapping
  public ResponseEntity<TripResponseList> getAllTrip(
      @AuthenticationPrincipal JwtAuthentication authentication) {

    List<Trip> trips = tripService.getByGuestId(authentication.id);

    return ok(TripResponseList.of(trips));
  }

  /* 여행 상세 조회 */
  @GetMapping("/{tripId}")
  public ResponseEntity<TripDetailedResponse> getDetailedTrip(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable Long tripId) {

    Trip trip = tripService.getDetailedById(tripId, authentication.id);

    return ok(TripDetailedResponse.of(trip));
  }

  /* 여행 취소 */
  @PostMapping("/{tripId}/cancel")
  public ResponseEntity<TripResponse> cancelTrip(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable Long tripId) {

    Trip trip = tripService.cancel(tripId, authentication.id);
    tripEmailService.send(authentication.email, trip.toMap(), MailKind.DELETE);
    return ok(TripResponse.of(trip));
  }
}
