package com.example.demo.unit.repository;

import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    @Transactional
    void itemStatus_notNull() {
        User owner = new User("admin", "testOwnerEmail", "testOwnerName", "testOwnerPassword", UserStatus.NORMAL);
        User manager = new User("admin", "testManagerEmail", "testManagerName", "testManagerPassword", UserStatus.NORMAL);
        userRepository.save(owner);
        userRepository.save(manager);

        Item item = new Item("testItemName", "testDescription", manager, owner);
        Item savedItem = itemRepository.save(item);
        Item statusNullItem = new Item(2L, "testItemName", "testDescription", manager, owner, null);

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> itemRepository.save(statusNullItem));
        assertNotNull(savedItem.getStatus());
        assertEquals("PENDING", savedItem.getStatus());
    }
}