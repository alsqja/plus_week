package com.example.demo.unit.service;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.QItem;
import com.example.demo.entity.QReservation;
import com.example.demo.entity.QUser;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReservationService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private JPAQueryFactory jpaQueryFactory;

    @Mock
    JPAQuery<Reservation> jpaQuery;

    @InjectMocks
    private ReservationService reservationService;

    LocalDateTime startedAt = LocalDateTime.of(2024, 11, 11, 13, 0, 0);
    LocalDateTime endAt = LocalDateTime.of(2024, 11, 13, 13, 0, 0);
    User owner = new User(1L, "admin", "owner@email.com", "ownerName", "password", UserStatus.NORMAL);
    User manager = new User(2L, "admin", "manager@email.com", "managerName", "password", UserStatus.NORMAL);
    User user = new User(3L, "user", "user@email.com", "userName", "password", UserStatus.NORMAL);
    Item item = new Item(1L, "name", "description", manager, owner, "PENDING");
    Reservation reservation = new Reservation(1L, item, user, ReservationStatus.PENDING, startedAt, endAt);

    @Test
    @DisplayName("예약 생성 성공 테스트")
    void createReservation_success() {

        when(itemRepository.findItemById(eq(1L))).thenReturn(item);
        when(userRepository.findUserById(eq(3L))).thenReturn(user);
        when(reservationRepository.findConflictingReservations(any(), any(), any())).thenReturn(new ArrayList<>());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationService.createReservation(1L, 3L, startedAt, endAt);

        assertEquals(1L, savedReservation.getId());
        assertEquals(user, savedReservation.getUser());
        assertEquals(item, savedReservation.getItem());
    }

    @Test
    @DisplayName("예약 생성 실패 테스트")
    void createReservation_fail() {

        List<Reservation> findConflictingReservationList = new ArrayList<>();
        findConflictingReservationList.add(reservation);
        when(reservationRepository.findConflictingReservations(any(), any(), any())).thenReturn(findConflictingReservationList);

        ReservationConflictException e = assertThrows(ReservationConflictException.class, () -> reservationService.createReservation(1L, 3L, startedAt, endAt));

        assertEquals("해당 물건은 이미 그 시간에 예약이 있습니다.", e.getMessage());
    }

    @Test
    void getReservations() {
        List<Reservation> findAllList = new ArrayList<>();
        findAllList.add(reservation);
        findAllList.add(reservation);
        findAllList.add(reservation);
        when(reservationRepository.findAll()).thenReturn(findAllList);

        List<ReservationResponseDto> findReservations = reservationService.getReservations();

        assertEquals(3, findReservations.size());
        assertEquals(1L, findReservations.get(0).getId());
    }

    @Test
    void searchAndConvertReservations() {
        QReservation reservation = QReservation.reservation;
        QUser qUser = QUser.user;
        QItem qItem = QItem.item;

        Reservation mockReservation = new Reservation(item, user, ReservationStatus.PENDING, startedAt, endAt);
        List<Reservation> mockReservations = List.of(mockReservation);

        given(jpaQueryFactory.selectFrom(reservation)).willReturn(jpaQuery);
        given(jpaQuery.leftJoin(reservation.user, qUser)).willReturn(jpaQuery);
        given(jpaQuery.fetchJoin()).willReturn(jpaQuery);
        given(jpaQuery.leftJoin(reservation.item, qItem)).willReturn(jpaQuery);
        given(jpaQuery.fetchJoin()).willReturn(jpaQuery);
        given(jpaQuery.where(any(BooleanBuilder.class))).willReturn(jpaQuery);
        given(jpaQuery.fetch()).willReturn(mockReservations);

        List<ReservationResponseDto> result = reservationService.searchAndConvertReservations(3L, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("userName", result.get(0).getNickname());
        assertEquals("name", result.get(0).getItemName());
    }

    @Test
    void searchReservations() {

        QReservation reservation = QReservation.reservation;
        QUser qUser = QUser.user;
        QItem qItem = QItem.item;

        Reservation mockReservation = new Reservation(item, user, ReservationStatus.PENDING, startedAt, endAt);
        List<Reservation> mockReservations = List.of(mockReservation);

        given(jpaQueryFactory.selectFrom(reservation)).willReturn(jpaQuery);
        given(jpaQuery.leftJoin(reservation.user, qUser)).willReturn(jpaQuery);
        given(jpaQuery.fetchJoin()).willReturn(jpaQuery);
        given(jpaQuery.leftJoin(reservation.item, qItem)).willReturn(jpaQuery);
        given(jpaQuery.fetchJoin()).willReturn(jpaQuery);
        given(jpaQuery.where(any(BooleanBuilder.class))).willReturn(jpaQuery);
        given(jpaQuery.fetch()).willReturn(mockReservations);

        List<Reservation> result = reservationService.searchReservations(1L, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("userName", result.get(0).getUser().getNickname());
        assertEquals("name", result.get(0).getItem().getName());
        assertEquals(user, result.get(0).getUser());
        assertEquals(item, result.get(0).getItem());
    }

    @Test
    @DisplayName("updateReservationStatus pending to approved 성공 테스트")
    void updateReservationStatus_pending_approved_success() {
        Long id = 1L;
        String status = "approved";
        when(reservationRepository.findReservationById(eq(id))).thenReturn(reservation);

        reservationService.updateReservationStatus(id, status);

        assertEquals(ReservationStatus.APPROVED, reservation.getStatus());
    }

    @Test
    @DisplayName("updateReservationStatus pending to canceled 성공 테스트")
    void updateReservationStatus_pending_canceled_success() {
        Long id = 1L;
        String status = "canceled";
        when(reservationRepository.findReservationById(eq(id))).thenReturn(reservation);

        reservationService.updateReservationStatus(id, status);

        assertEquals(ReservationStatus.CANCELED, reservation.getStatus());
    }

    @Test
    @DisplayName("updateReservationStatus pending to expired 성공 테스트")
    void updateReservationStatus_pending_expired_success() {
        Long id = 1L;
        String status = "expired";
        when(reservationRepository.findReservationById(eq(id))).thenReturn(reservation);

        reservationService.updateReservationStatus(id, status);

        assertEquals(ReservationStatus.EXPIRED, reservation.getStatus());
    }

    @Test
    @DisplayName("updateReservationStatus expired 실패 테스트")
    void updateReservationStatus_expired_fail() {
        Long id = 1L;
        Reservation reservation = new Reservation(1L, item, user, ReservationStatus.EXPIRED, startedAt, endAt);
        when(reservationRepository.findReservationById(eq(id))).thenReturn(reservation);

        IllegalArgumentException cancelErr = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(id, "canceled"));
        IllegalArgumentException approvedErr = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(id, "approved"));
        IllegalArgumentException expiredErr = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(id, "expired"));

        assertEquals("EXPIRED 상태인 예약은 취소할 수 없습니다.", cancelErr.getMessage());
        assertEquals("PENDING 상태만 APPROVED로 변경 가능합니다.", approvedErr.getMessage());
        assertEquals("PENDING 상태만 EXPIRED로 변경 가능합니다.", expiredErr.getMessage());
    }

    @Test
    @DisplayName("updateReservationStatus canceled 테스트")
    void updateReservationStatus_canceled() {
        Long id = 1L;
        Reservation reservation = new Reservation(1L, item, user, ReservationStatus.CANCELED, startedAt, endAt);
        when(reservationRepository.findReservationById(eq(id))).thenReturn(reservation);

        IllegalArgumentException approvedErr = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(id, "approved"));
        IllegalArgumentException expiredErr = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(id, "expired"));
        IllegalArgumentException pendingErr = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(id, "pending"));

        assertEquals("PENDING 상태만 APPROVED로 변경 가능합니다.", approvedErr.getMessage());
        assertEquals("PENDING 상태만 EXPIRED로 변경 가능합니다.", expiredErr.getMessage());
        assertEquals("올바르지 않은 상태: pending", pendingErr.getMessage());
    }
}