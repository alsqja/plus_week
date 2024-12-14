package com.example.demo.unit.entity;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    User user = new User(1L, "user", "email", "nickname", "password", UserStatus.NORMAL);

    @Test
    void getId() {
        assertEquals(1L, user.getId());
    }

    @Test
    void getEmail() {
        assertEquals("email", user.getEmail());
    }

    @Test
    void getNickname() {
        assertEquals("nickname", user.getNickname());
    }

    @Test
    void getPassword() {
        assertEquals("password", user.getPassword());
    }

    @Test
    void getStatus() {
        assertEquals(UserStatus.NORMAL, user.getStatus());
    }

    @Test
    void getRole() {
        assertEquals(Role.USER, user.getRole());
    }
}