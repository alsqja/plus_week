package com.example.demo.unit.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void reportUsers() {
        List<Long> userIds = List.of(new Long[]{1L, 2L, 3L});
        List<Long> wrongIds = List.of(new Long[]{4L, 5L, 6L});
        List<User> users = List.of(new User[]{new User(), new User(), new User()});
        List<User> wrongUsers = List.of(new User[]{new User(), new User()});
        when(userRepository.findByIdIn(eq(userIds))).thenReturn(users);
        when(userRepository.findByIdIn(eq(wrongIds))).thenReturn(wrongUsers);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> adminService.reportUsers(wrongIds));
        adminService.reportUsers(userIds);

        assertEquals("해당 ID에 맞는 값이 존재하지 않습니다.", e.getMessage());
        verify(userRepository).findByIdIn(eq(userIds));
        verify(userRepository).updateAllUserStatus(eq(UserStatus.BLOCKED), eq(userIds));
    }
}