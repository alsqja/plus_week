package com.example.demo.unit.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void findByEmail() {
        User user1 = new User("admin", "email1@email.com", "name1", "password", UserStatus.NORMAL);
        User savedUser = userRepository.save(user1);

        User findUser = userRepository.findByEmail("email1@email.com");
        User initUser = userRepository.findByEmail("email1");

        assertEquals(1L, initUser.getId());
        assertEquals(savedUser.getId(), findUser.getId());
        assertEquals("name1", findUser.getNickname());
    }

    @Test
    void findByIdIn() {
        User user1 = new User("admin", "email1@email.com", "name1", "password", UserStatus.NORMAL);
        User user2 = new User("admin", "email2@email.com", "name2", "password", UserStatus.NORMAL);
        User user3 = new User("user", "email3@email.com", "name3", "password", UserStatus.NORMAL);
        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);
        User savedUser3 = userRepository.save(user3);
        List<Long> userIds = List.of(new Long[]{savedUser1.getId(), savedUser2.getId(), savedUser3.getId()});

        List<User> users = userRepository.findByIdIn(userIds);

        assertEquals(3, users.size());
        assertEquals(savedUser1.getId(), users.get(0).getId());
    }

    @Test
    void updateAllUserStatus() {
        User user1 = new User("admin", "email1@email.com", "name1", "password", UserStatus.NORMAL);
        User user2 = new User("admin", "email2@email.com", "name2", "password", UserStatus.NORMAL);
        User user3 = new User("user", "email3@email.com", "name3", "password", UserStatus.NORMAL);
        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);
        User savedUser3 = userRepository.save(user3);
        List<Long> userIds = List.of(new Long[]{savedUser1.getId(), savedUser2.getId(), savedUser3.getId()});

        userRepository.updateAllUserStatus(UserStatus.BLOCKED, userIds);

        entityManager.clear();

        List<User> users = userRepository.findByIdIn(userIds);

        assertEquals(3, users.size());
        assertEquals(UserStatus.BLOCKED, users.get(0).getStatus());
    }

    @Test
    void findUserById() {
        User user1 = new User("admin", "email1@email.com", "name1", "password", UserStatus.NORMAL);
        User savedUser = userRepository.save(user1);
        User user = userRepository.findUserById(savedUser.getId());

        InvalidDataAccessApiUsageException e = assertThrows(InvalidDataAccessApiUsageException.class, () -> userRepository.findUserById(0L));

        assertNotNull(user);
        assertEquals("email1@email.com", user.getEmail());
        assertEquals("해당 ID에 맞는 값이 존재하지 않습니다.", e.getMessage());
    }
}