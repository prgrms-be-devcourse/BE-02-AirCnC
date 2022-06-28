package com.gurudev.aircnc.controller;


import static org.springframework.http.HttpStatus.CREATED;

import com.gurudev.aircnc.controller.dto.TripDto.TripReserveRequest;
import com.gurudev.aircnc.controller.dto.TripDto.TripReserveRequest.Request;
import com.gurudev.aircnc.controller.dto.TripDto.TripResponse;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.service.TripService;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand;
import com.gurudev.aircnc.infrastructure.security.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    return new ResponseEntity<>(TripResponse.of(trip), CREATED);
  }
}
