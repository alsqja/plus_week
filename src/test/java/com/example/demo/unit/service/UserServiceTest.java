package com.example.demo.unit.service;

import com.example.demo.dto.Authentication;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void signupWithEmail() {
        String testRole = "user";
        String testNickname = "testNickname";
        String testEmail = "test@email.com";
        String testPassword = "testPassword";
        UserRequestDto dto = new UserRequestDto(testRole, testEmail, testNickname, testPassword);
        User mockUser = new User(1L, testRole, testEmail, testNickname, PasswordEncoder.encode(testPassword), UserStatus.NORMAL);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User savedUser = userService.signupWithEmail(dto);

        assertEquals(1L, savedUser.getId());
        assertEquals(testNickname, savedUser.getNickname());
        assertEquals(testEmail, savedUser.getEmail());
        assertNotEquals(testPassword, savedUser.getPassword());
    }

    @Test
    void loginUser() {
        String testRole = "user";
        String testNickname = "testNickname";
        String testEmail = "test@email.com";
        String wrongEmail = "wrong@email.com";
        String testPassword = "testPassword";
        String wrongPassword = "wrongPassword";
        User mockUser = new User(1L, testRole, testEmail, testNickname, PasswordEncoder.encode(testPassword), UserStatus.NORMAL);
        when(userRepository.findByEmail(eq(testEmail))).thenReturn(mockUser);
        when(userRepository.findByEmail(eq(wrongEmail))).thenReturn(null);
        LoginRequestDto dto = new LoginRequestDto(testEmail, testPassword);
        LoginRequestDto wrongPasswordDto = new LoginRequestDto(testEmail, wrongPassword);
        LoginRequestDto wrongEmailDto = new LoginRequestDto(wrongEmail, testPassword);

        Authentication success = userService.loginUser(dto);
        ResponseStatusException wrongEmailErr = assertThrows(ResponseStatusException.class, () -> userService.loginUser(wrongEmailDto));
        ResponseStatusException wrongPasswordErr = assertThrows(ResponseStatusException.class, () -> userService.loginUser(wrongPasswordDto));

        assertEquals(1L, success.getId());
        assertEquals(Role.USER, success.getRole());
        assertEquals("유효하지 않은 사용자 이름 혹은 잘못된 비밀번호", wrongEmailErr.getReason());
        assertEquals("유효하지 않은 사용자 이름 혹은 잘못된 비밀번호", wrongPasswordErr.getReason());
    }
}