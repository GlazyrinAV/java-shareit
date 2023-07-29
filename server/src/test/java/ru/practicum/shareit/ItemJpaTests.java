package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ItemJpaTests {

    @Autowired
    private TestEntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void findAllWhereOwnerIdInWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Pageable page = PageRequest.of(0, 1);
        assertEquals(List.of(item1), itemRepository.findAllWhereOwnerIdIn(1, page).getContent());
    }

    @Test
    void findAllWhereOwnerIdInWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User user = new User(2, "User2", "email2@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Item item2 = new Item(2, "Item2", "description2", true, owner, null);
        Item item3 = new Item(3, "Item3", "description3", false, owner, null);
        ItemRequest request = new ItemRequest(1, user, "request_description", LocalDateTime.of(2022,7, 20, 9, 0), null);
        Item item4 = new Item(4, "Item4", "description4", true, owner, request);
        request.setItems(List.of(item4));
        assertEquals(List.of(item1, item2, item3, item4), itemRepository.findAllWhereOwnerIdIn(1));
    }

    @Test
    void findByNameWithPage() {
        User owner = new User(1, "User1", "email1@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Pageable page = PageRequest.of(0, 1);
        assertEquals(List.of(item1), itemRepository.findByName("ItEm", page).getContent());
    }

    @Test
    void findByNameWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User user = new User(2, "User2", "email2@email.com");
        Item item1 = new Item(1, "Item1", "description1", true, owner, null);
        Item item2 = new Item(2, "Item2", "description2", true, owner, null);
        ItemRequest request = new ItemRequest(1, user, "request_description", LocalDateTime.of(2022,7, 20, 9, 0), null);
        Item item4 = new Item(4, "Item4", "description4", true, owner, request);
        request.setItems(List.of(item4));
        assertEquals(List.of(item1, item2, item4), itemRepository.findByName("ItEm"));
    }

}