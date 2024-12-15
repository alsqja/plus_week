package com.example.demo.unit.repository;

import com.example.demo.entity.Item;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;

    private Item item;

    private LocalDateTime startAt = LocalDateTime.of(2024, 11, 11, 13, 0, 0);
    private LocalDateTime endAt = LocalDateTime.of(2024, 11, 12, 13, 0, 0);

    @BeforeEach
    void init() {
        User owner = new User("admin", "testOwnerEmail", "testOwnerName", "testOwnerPassword", UserStatus.NORMAL);
        User manager = new User("admin", "testManagerEmail", "testManagerName", "testManagerPassword", UserStatus.NORMAL);
        userRepository.save(owner);
        userRepository.save(manager);

        Item item = new Item("testItemName", "testDescription", manager, owner);
        this.item = itemRepository.save(item);

        User user = new User("user", "email@email.com", "userName", "password", UserStatus.NORMAL);
        this.user = userRepository.save(user);
    }

    @AfterEach
    void cleanup() {
        reservationRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void findConflictingReservations() {
        Reservation reservation = new Reservation(item, user, ReservationStatus.APPROVED, startAt, endAt);
        Reservation savedReservation = reservationRepository.save(reservation);

        List<Reservation> reservations = reservationRepository.findConflictingReservations(item.getId(), startAt, endAt);

        assertEquals(1, reservations.size());
        assertEquals(item.getId(), reservations.get(0).getItem().getId());
        assertEquals(user, reservations.get(0).getUser());
    }

    @Test
    void findAll() {
        Reservation reservation = new Reservation(item, user, ReservationStatus.APPROVED, startAt, endAt);
        Reservation savedReservation = reservationRepository.save(reservation);

        List<Reservation> reservations = reservationRepository.findAll();

        assertEquals(1, reservations.size());
        assertEquals(item.getId(), reservations.get(0).getItem().getId());
        assertEquals(user, reservations.get(0).getUser());
    }

    @Test
    void findReservationById() {
        Reservation reservation = new Reservation(item, user, ReservationStatus.APPROVED, startAt, endAt);
        Reservation savedReservation = reservationRepository.save(reservation);

        InvalidDataAccessApiUsageException e = assertThrows(InvalidDataAccessApiUsageException.class, () -> reservationRepository.findReservationById(0L));
        Reservation findReservation = reservationRepository.findReservationById(savedReservation.getId());

        assertEquals(savedReservation.getId(), findReservation.getId());
        assertEquals(savedReservation.getUser(), findReservation.getUser());
        assertEquals(item, findReservation.getItem());
        assertEquals("해당 ID에 맞는 값이 존재하지 않습니다.", e.getMessage());
    }
}