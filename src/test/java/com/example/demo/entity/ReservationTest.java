package com.example.demo.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationTest {

    User user = new User(1L, "user", "email", "nickname", "password", UserStatus.NORMAL);
    User owner = new User("admin", "testOwnerEmail", "testOwnerName", "testOwnerPassword", UserStatus.NORMAL);
    User manager = new User("admin", "testManagerEmail", "testManagerName", "testManagerPassword", UserStatus.NORMAL);
    Item item = new Item(1L, "testItemName", "testDescription", manager, owner, "PENDING");
    Reservation reservation = new Reservation(1L, item, user, ReservationStatus.PENDING, LocalDateTime.of(2024, 11, 11, 13, 0, 0), LocalDateTime.of(2024, 11, 13, 13, 0, 0));

    @Test
    void updateStatus() {
        // Given
        ReservationStatus newStatus = ReservationStatus.APPROVED;

        // When
        reservation.updateStatus(newStatus);

        // Then
        assertEquals(newStatus, reservation.getStatus());
    }

    @Test
    void getId() {
        assertEquals(1L, reservation.getId());
    }

    @Test
    void getItem() {
        assertEquals(item, reservation.getItem());
    }

    @Test
    void getUser() {
        assertEquals(user, reservation.getUser());
    }

    @Test
    void getStartAt() {
        assertEquals(LocalDateTime.of(2024, 11, 11, 13, 0, 0), reservation.getStartAt());
    }

    @Test
    void getEndAt() {
        assertEquals(LocalDateTime.of(2024, 11, 13, 13, 0, 0), reservation.getEndAt());
    }

    @Test
    void getStatus() {
        assertEquals(ReservationStatus.PENDING, reservation.getStatus());
    }
}