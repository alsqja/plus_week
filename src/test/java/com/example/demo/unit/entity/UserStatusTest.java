package com.example.demo.unit.entity;

import com.example.demo.entity.UserStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserStatusTest {

    @Test
    void of() {
        UserStatus status = UserStatus.of("normal");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> UserStatus.of("asd"));

        assertEquals("해당되는 이름의 상태를 찾을 수 없습니다. asd", e.getMessage());
        assertEquals(UserStatus.NORMAL, status);
    }
}