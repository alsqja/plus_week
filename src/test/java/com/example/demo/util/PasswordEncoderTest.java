package com.example.demo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    @Test
    void encode() {
        // given
        String rawPassword = "testPassword";

        // when
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        // then
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
    }

    @Test
    void matches_success() {
        // given
        String rawPassword = "testPassword";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        // when
        boolean result = PasswordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertTrue(result);
    }

    @Test
    void matches_fail() {
        // given
        String rawPassword = "testPassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        // when
        boolean result = PasswordEncoder.matches(wrongPassword, encodedPassword);

        // then
        assertFalse(result); // 잘못된 비밀번호는 일치하지 않아야 함
    }
}
