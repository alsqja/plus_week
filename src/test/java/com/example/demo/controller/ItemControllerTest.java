package com.example.demo.controller;

import com.example.demo.config.WebConfig;
import com.example.demo.dto.ItemRequestDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ItemController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        WebConfig.class
                }
        ))
class ItemControllerTest {

    @MockitoBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createItem() throws Exception {
        User owner = new User(1L, "admin", "testOwnerEmail", "testOwnerName", "testOwnerPassword", UserStatus.NORMAL);
        User manager = new User(2L, "admin", "testManagerEmail", "testManagerName", "testManagerPassword", UserStatus.NORMAL);
        ItemRequestDto dto = new ItemRequestDto("name", "description", 2L, 1L);
        Item item = new Item(1L, "name", "description", owner, manager, "PENDING");
        when(itemService.createItem(eq(dto.getName()), eq(dto.getDescription()), eq(dto.getOwnerId()), eq(dto.getManagerId()))).thenReturn(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("create item"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("name"));
    }
}