package com.example.demo.dto;

import com.example.demo.dto.common.BaseResDtoType;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationResDto implements BaseResDtoType {

    private final Long id;
    private final Long itemId;
    private final Long userId;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final ReservationStatus status;

    public ReservationResDto(Reservation reservation) {
        this.id = reservation.getId();
        this.itemId = reservation.getItem().getId();
        this.userId = reservation.getUser().getId();
        this.startAt = reservation.getStartAt();
        this.endAt = reservation.getEndAt();
        this.status = reservation.getStatus();
    }

}
