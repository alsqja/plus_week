package com.example.demo.integration.service;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReservationService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ReservationServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("createReservation 통합 테스트")
    void createReservationTest() {
        Long itemId = 1L;
        Long userId = 2L;
        LocalDateTime startAt = LocalDateTime.of(2024, 11, 11, 13, 0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 11, 14, 13, 0, 0);

        Reservation saved = reservationService.createReservation(itemId, userId, startAt, endAt);
        saved.updateStatus(ReservationStatus.APPROVED);

        em.flush();
        em.clear();

        ReservationConflictException e = assertThrows(
                ReservationConflictException.class,
                () -> reservationService.createReservation(itemId, userId, startAt.plusDays(1), endAt.minusDays(1))
        );

        assertNotNull(saved);
        assertEquals(itemId, saved.getItem().getId());
        assertEquals(userId, saved.getUser().getId());
        assertEquals("해당 물건은 이미 그 시간에 예약이 있습니다.", e.getMessage());
    }

    @Test
    void getReservationsTest() {
        List<ReservationResponseDto> result = reservationService.getReservations();

        assertEquals(4, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("name2", result.get(0).getNickname());
    }

    @Test
    void searchAndConvertReservationTest() {
        List<ReservationResponseDto> result1 = reservationService.searchAndConvertReservations(1L, 1L); // 1
        List<ReservationResponseDto> result2 = reservationService.searchAndConvertReservations(1L, 2L); // 0
        List<ReservationResponseDto> result3 = reservationService.searchAndConvertReservations(2L, 1L); // 1
        List<ReservationResponseDto> result4 = reservationService.searchAndConvertReservations(2L, 2L); // 1
        List<ReservationResponseDto> result5 = reservationService.searchAndConvertReservations(null, 2L); // 1
        List<ReservationResponseDto> result6 = reservationService.searchAndConvertReservations(2L, null); // 2
        List<ReservationResponseDto> result7 = reservationService.searchAndConvertReservations(null, null); // 3

        assertEquals(2, result1.size());
        assertEquals(0, result2.size());
        assertEquals(1, result3.size());
        assertEquals(1, result4.size());
        assertEquals(1, result5.size());
        assertEquals(2, result6.size());
        assertEquals(4, result7.size());
    }

    @Test
    void updateReservation_success() {
        reservationService.updateReservationStatus(1L, "approved"); // pending -> approved
        reservationService.updateReservationStatus(2L, "canceled"); // approved -> canceled

        em.flush();
        em.clear();

        Reservation result1 = reservationRepository.findReservationById(1L);
        Reservation result2 = reservationRepository.findReservationById(2L);

        assertEquals(ReservationStatus.APPROVED, result1.getStatus());
        assertEquals(ReservationStatus.CANCELED, result2.getStatus());
    }

    @Test
    void updateReservationStatus_fail() {
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(2L, "approved"));
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(3L, "canceled"));
        IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(2L, "expired"));
        IllegalArgumentException e4 = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(2L, "asd"));

        assertEquals("PENDING 상태만 APPROVED로 변경 가능합니다.", e1.getMessage());
        assertEquals("EXPIRED 상태인 예약은 취소할 수 없습니다.", e2.getMessage());
        assertEquals("PENDING 상태만 EXPIRED로 변경 가능합니다.", e3.getMessage());
        assertEquals("올바르지 않은 상태: asd", e4.getMessage());
    }
}
