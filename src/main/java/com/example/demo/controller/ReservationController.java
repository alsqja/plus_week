package com.example.demo.controller;

import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.dto.common.CommonListResDto;
import com.example.demo.dto.common.CommonResDto;
import com.example.demo.entity.Reservation;
import com.example.demo.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<CommonResDto<ReservationResDto>> createReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
        Reservation reservation = reservationService.createReservation(
                reservationRequestDto.getItemId(),
                reservationRequestDto.getUserId(),
                reservationRequestDto.getStartAt(),
                reservationRequestDto.getEndAt()
        );

        return ResponseEntity.status(201).body(new CommonResDto<>("create reservation", new ReservationResDto(reservation)));
    }

    @PatchMapping("/{id}/update-status")
    public ResponseEntity<Void> updateReservation(@PathVariable Long id, @RequestBody String status) {
        reservationService.updateReservationStatus(id, status);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<CommonListResDto<ReservationResponseDto>> findAll() {
        List<ReservationResponseDto> result = reservationService.getReservations();

        return ResponseEntity.ok().body(new CommonListResDto<>("find all reservations", result));
    }

    @GetMapping("/search")
    public ResponseEntity<CommonListResDto<ReservationResponseDto>> searchAll(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId
    ) {
        List<ReservationResponseDto> result = reservationService.searchAndConvertReservations(userId, itemId);

        return ResponseEntity.ok().body(new CommonListResDto<>("search reservations", result));
    }
}
