package com.example.demo.unit.entity;

import com.example.demo.entity.ReservationStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationStatusTest {

    @Test
    void of() {
        ReservationStatus status = ReservationStatus.of("pending");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ReservationStatus.of("asd"));

        assertEquals("해당되는 이름의 상태를 찾을 수 없습니다. asd", e.getMessage());
        assertEquals(ReservationStatus.PENDING, status);
    }
}