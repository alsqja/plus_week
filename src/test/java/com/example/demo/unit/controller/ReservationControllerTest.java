package com.example.demo.unit.controller;

import com.example.demo.config.WebConfig;
import com.example.demo.controller.ReservationController;
import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ReservationController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        WebConfig.class
                }
        ))
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    LocalDateTime startedAt = LocalDateTime.of(2024, 11, 11, 13, 0, 0);
    LocalDateTime endAt = LocalDateTime.of(2024, 11, 13, 13, 0, 0);

    @Test
    void createReservation() throws Exception {
        User owner = new User(1L, "admin", "testOwnerEmail", "testOwnerName", "testOwnerPassword", UserStatus.NORMAL);
        User manager = new User(2L, "admin", "testManagerEmail", "testManagerName", "testManagerPassword", UserStatus.NORMAL);
        User user = new User(3L, "user", "testUserEmail", "testUserName", "testUserPassword", UserStatus.NORMAL);
        Item item = new Item(1L, "name", "description", owner, manager, "PENDING");
        Reservation mockReservation = new Reservation(1L, item, user, ReservationStatus.PENDING, startedAt, endAt);
        ReservationRequestDto dto = new ReservationRequestDto(item.getId(), user.getId(), startedAt, endAt);
        when(reservationService.createReservation(eq(dto.getItemId()), eq(dto.getUserId()), eq(dto.getStartAt()), eq(dto.getEndAt()))).thenReturn(mockReservation);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("create reservation"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.itemId").value(1L))
                .andExpect(jsonPath("$.data.userId").value(3L))
                .andExpect(jsonPath("$.data.status").value(ReservationStatus.PENDING.name()));
    }

    @Test
    void updateReservation() throws Exception {

        mockMvc.perform(patch("/reservations/1/update-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("approved")))
                .andExpect(status().isNoContent());
    }

    @Test
    void findAll() throws Exception {
        List<ReservationResponseDto> mockReservations = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            mockReservations.add(new ReservationResponseDto((long) i, "nickname" + i, "itemName" + i, startedAt, endAt));
        }
        when(reservationService.getReservations()).thenReturn(mockReservations);

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("find all reservations"))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void searchAll() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        List<ReservationResponseDto> mockReservations = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            mockReservations.add(new ReservationResponseDto((long) i, "nickname" + i, "itemName" + i, startedAt, endAt));
        }
        when(reservationService.searchAndConvertReservations(eq(userId), eq(itemId))).thenReturn(mockReservations);


        mockMvc.perform(get("/reservations/search")
                        .param("userId", objectMapper.writeValueAsString(userId))
                        .param("itemId", objectMapper.writeValueAsString(itemId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("search reservations"))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].id").value(1L));
    }
}