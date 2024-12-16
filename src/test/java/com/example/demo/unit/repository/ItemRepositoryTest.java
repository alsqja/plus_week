package com.example.demo.unit.repository;

import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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

    @AfterEach
    void cleanup() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void itemStatus_notNull() {
        User owner = new User("admin", "testOwnerEmail", "testOwnerName", "testOwnerPassword", UserStatus.NORMAL);
        User manager = new User("admin", "testManagerEmail", "testManagerName", "testManagerPassword", UserStatus.NORMAL);
        userRepository.save(owner);
        userRepository.save(manager);

        Item item = new Item("testItemName", "testDescription", manager, owner);
        Item savedItem = itemRepository.save(item);
        Item statusNullItem = new Item(0L, "testItemName", "testDescription", manager, owner, null);

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> itemRepository.save(statusNullItem));
        assertNotNull(savedItem.getStatus());
        assertEquals("PENDING", savedItem.getStatus());
    }

    @Test
    void findItemByIdTest() {
        User owner = new User("admin", "testOwnerEmail", "testOwnerName", "testOwnerPassword", UserStatus.NORMAL);
        User manager = new User("admin", "testManagerEmail", "testManagerName", "testManagerPassword", UserStatus.NORMAL);
        userRepository.save(owner);
        userRepository.save(manager);

        Item item = new Item("testItemName", "testDescription", manager, owner);
        Item savedItem = itemRepository.save(item);

        Item findItem = itemRepository.findItemById(savedItem.getId());

        InvalidDataAccessApiUsageException e = assertThrows(InvalidDataAccessApiUsageException.class, () -> itemRepository.findItemById(0L));

        assertEquals(savedItem.getId(), findItem.getId());
        assertEquals(manager, findItem.getManager());
        assertEquals("해당 ID에 맞는 값이 존재하지 않습니다.", e.getMessage());
    }
}