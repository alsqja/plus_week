package com.example.demo.unit.entity;

import com.example.demo.entity.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleTest {

    @Test
    void of() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Role.of("asd"));

        assertEquals("해당하는 이름의 권한을 찾을 수 없습니다: asd", e.getMessage());
    }
}