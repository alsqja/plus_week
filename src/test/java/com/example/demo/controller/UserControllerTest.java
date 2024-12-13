package com.example.demo.controller;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.dto.Authentication;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signupWithEmailTest() throws Exception {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("user", "email@example.com", "password", "nickname");
        User mockUser = new User("user", "email@email.com", "nickname", "password", UserStatus.NORMAL);
        Mockito.when(userService.signupWithEmail(any(UserRequestDto.class))).thenReturn(mockUser);

        // when, then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("signup"))
                .andExpect(jsonPath("$.data.email").value(mockUser.getEmail()));
    }

    @Test
    void loginWithEmail() throws Exception {
        // given
        LoginRequestDto dto = new LoginRequestDto("email@email.com", "password");
        Authentication mockAuth = new Authentication(1L, Role.USER);
        Mockito.when(userService.loginUser(any(LoginRequestDto.class))).thenReturn(mockAuth);

        MockHttpSession session = new MockHttpSession();

        // when, then
        mockMvc.perform(post("/users/login")
                        .session(session)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("login"))
                .andExpect(jsonPath("$.data.id").value(mockAuth.getId()))
                .andExpect(jsonPath("$.data.role").value(mockAuth.getRole().toString()));

        assertNotNull(session.getAttribute(GlobalConstants.USER_AUTH));
        assertEquals(mockAuth, session.getAttribute(GlobalConstants.USER_AUTH));
    }

    @Test
    void logout() throws Exception {
        Authentication mockAuth = new Authentication(1L, Role.USER);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(GlobalConstants.USER_AUTH, mockAuth);

        mockMvc.perform(post("/users/logout")
                        .session(session))
                .andExpect(status().isNoContent());

        assertTrue(session.isInvalid());
    }
}