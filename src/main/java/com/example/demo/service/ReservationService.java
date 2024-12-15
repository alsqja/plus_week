package com.example.demo.service;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.QItem;
import com.example.demo.entity.QReservation;
import com.example.demo.entity.QUser;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.User;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public ReservationService(
            ReservationRepository reservationRepository,
            ItemRepository itemRepository,
            UserRepository userRepository,
            JPAQueryFactory jpaQueryFactory
    ) {
        this.reservationRepository = reservationRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // TODO: 1. 트랜잭션 이해
    @Transactional
    public Reservation createReservation(Long itemId, Long userId, LocalDateTime startAt, LocalDateTime endAt) {

        List<Reservation> haveReservations = reservationRepository.findConflictingReservations(itemId, startAt, endAt);
        if (!haveReservations.isEmpty()) {
            throw new ReservationConflictException("해당 물건은 이미 그 시간에 예약이 있습니다.");
        }

        Item item = itemRepository.findItemById(itemId);
        User user = userRepository.findUserById(userId);
        Reservation reservation = new Reservation(item, user, ReservationStatus.PENDING, startAt, endAt);

        return reservationRepository.save(reservation);
    }

    // TODO: 3. N+1 문제
    public List<ReservationResponseDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream().map(reservation -> {
            User user = reservation.getUser();
            Item item = reservation.getItem();

            return new ReservationResponseDto(
                    reservation.getId(),
                    user.getNickname(),
                    item.getName(),
                    reservation.getStartAt(),
                    reservation.getEndAt()
            );
        }).toList();
    }

    // TODO: 5. QueryDSL 검색 개선
    public List<ReservationResponseDto> searchAndConvertReservations(Long userId, Long itemId) {

        List<Reservation> reservations = searchReservations(userId, itemId);

        return convertToDto(reservations);
    }

    public List<Reservation> searchReservations(Long userId, Long itemId) {

        QUser user = QUser.user;
        QItem item = QItem.item;
        QReservation reservation = QReservation.reservation;
        BooleanBuilder builder = new BooleanBuilder();

        if (userId != null) {
            builder.and(user.id.eq(userId));
        }
        if (itemId != null) {
            builder.and(item.id.eq(itemId));
        }

        return jpaQueryFactory
                .selectFrom(reservation)
                .leftJoin(reservation.user, user)
                .fetchJoin()
                .leftJoin(reservation.item, item)
                .fetchJoin()
                .where(builder)
                .fetch();
    }

    private List<ReservationResponseDto> convertToDto(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> new ReservationResponseDto(
                        reservation.getId(),
                        reservation.getUser().getNickname(),
                        reservation.getItem().getName(),
                        reservation.getStartAt(),
                        reservation.getEndAt()
                ))
                .toList();
    }

    // TODO: 7. 리팩토링
    @Transactional
    public void updateReservationStatus(Long reservationId, String status) {
        Reservation reservation = reservationRepository.findReservationById(reservationId);

        if (ReservationStatus.APPROVED.getName().equals(status)) {
            if (!ReservationStatus.PENDING.equals(reservation.getStatus())) {
                throw new IllegalArgumentException("PENDING 상태만 APPROVED로 변경 가능합니다.");
            }
            reservation.updateStatus(ReservationStatus.APPROVED);
            return;
        }

        if (ReservationStatus.CANCELED.getName().equals(status)) {
            if (ReservationStatus.EXPIRED.equals(reservation.getStatus())) {
                throw new IllegalArgumentException("EXPIRED 상태인 예약은 취소할 수 없습니다.");
            }
            reservation.updateStatus(ReservationStatus.CANCELED);
            return;
        }

        if (ReservationStatus.EXPIRED.getName().equals(status)) {
            if (!ReservationStatus.PENDING.equals(reservation.getStatus())) {
                throw new IllegalArgumentException("PENDING 상태만 EXPIRED로 변경 가능합니다.");
            }
            reservation.updateStatus(ReservationStatus.EXPIRED);
            return;
        }
//        pending 으로 변경 x
//        if (ReservationStatus.PENDING.getName().equals(status)) {
//            reservation.updateStatus(ReservationStatus.PENDING);
//            return;
//        }

        throw new IllegalArgumentException("올바르지 않은 상태: " + status);

    }
}
