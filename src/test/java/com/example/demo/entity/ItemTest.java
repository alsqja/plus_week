package com.example.demo.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class ItemTest {

    User owner = new User("admin", "testOwnerEmail", "testOwnerName", "testOwnerPassword", UserStatus.NORMAL);
    User manager = new User("admin", "testManagerEmail", "testManagerName", "testManagerPassword", UserStatus.NORMAL);
    Item item = new Item(1L, "testItemName", "testDescription", manager, owner, "PENDING");

    @Test
    void getId() {
        assertEquals(1L, item.getId());
    }

    @Test
    void getName() {
        assertEquals("testItemName", item.getName());
    }

    @Test
    void getDescription() {
        assertEquals("testDescription", item.getDescription());
    }

    @Test
    void getOwner() {
        assertEquals(owner, item.getOwner());
    }

    @Test
    void getManager() {
        assertEquals(manager, item.getManager());
    }

    @Test
    void getStatus() {
        assertNotNull(item.getStatus());
        assertEquals("PENDING", item.getStatus());
    }
}