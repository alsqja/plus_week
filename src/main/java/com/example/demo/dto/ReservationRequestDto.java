package com.example.demo.dto;

import lombok.Generated;
import lombok.Getter;
import org.jetbrains.annotations.TestOnly;

import java.time.LocalDateTime;

@Getter
public class ReservationRequestDto {
    private Long itemId;
    private Long userId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @TestOnly
    @Generated
    public ReservationRequestDto(Long itemId, Long userId, LocalDateTime startAt, LocalDateTime endAt) {
        this.itemId = itemId;
        this.userId = userId;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
