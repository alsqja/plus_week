package com.example.demo.unit.service;

import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void createItem() {
        Long ownerId = 1L;
        Long managerId = 2L;
        User owner = new User(ownerId, "admin", "owner@email.com", "ownerName", "password", UserStatus.NORMAL);
        User manager = new User(managerId, "admin", "manager@email.com", "managerName", "password", UserStatus.NORMAL);
        String name = "name";
        String description = "description";
        Item item = new Item(1L, name, description, manager, owner, "PENDING");
        when(userRepository.findUserById(eq(ownerId))).thenReturn(owner);
        when(userRepository.findUserById(eq(managerId))).thenReturn(manager);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item savedItem = itemService.createItem(name, description, ownerId, managerId);

        assertEquals(1L, savedItem.getId());
        assertEquals(name, savedItem.getName());
        assertEquals(description, savedItem.getDescription());
        assertEquals(owner, savedItem.getOwner());
        assertEquals(manager, savedItem.getManager());
    }
}