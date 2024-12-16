package com.example.demo.config;

import com.example.demo.entity.Item;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.PasswordEncoder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class DataInitializer {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ReservationRepository reservationRepository;

    @PostConstruct
    public void init() {

        for (int i = 0; i < 3; i++) {
            User adminUser = new User("admin", "email" + i, "nickname" + i, PasswordEncoder.encode("0000"), UserStatus.NORMAL);
            userRepository.save(adminUser);
        }

        for (int i = 3; i < 6; i++) {
            User user = new User("user", "email" + i, "nickname" + i, PasswordEncoder.encode("0000"), UserStatus.NORMAL);
            userRepository.save(user);
        }

        for (int i = 1; i <= 6; i++) {
            User user = userRepository.findById((long) i).get();
            Item item = new Item("item" + i, "description" + i, user, user);

            itemRepository.save(item);
        }

        for (int i = 0; i < 10; i++) {
            Long randomUserId = (long) ((int) (Math.random() * 3) + 4);
            Long randomItemId = (long) ((int) (Math.random() * 6) + 1);
            User user = userRepository.findById(randomUserId).get();
            Item item = itemRepository.findById(randomItemId).get();

            Reservation reservation = new Reservation(item, user, null, LocalDateTime.of(2024, 11, 11, 13, 0, 0), LocalDateTime.of(2024, 12, 11, 13, 0, 0));
            reservationRepository.save(reservation);
        }
    }
}
