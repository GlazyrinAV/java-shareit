package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
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
class ItemRequestJpaTests {

    @Autowired
    private TestEntityManager em;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void findAll() {
        User owner = new User(1, "User1", "email1@email.com");
        User user1 = new User(2, "User2", "email2@email.com");
        User user2 = new User(3, "User3", "email3@email.com");
        ItemRequest request1 = new ItemRequest(1, user1, "request_description", LocalDateTime.of(2022,7, 20, 9, 0), null);
        Item item4 = new Item(4, "Item4", "description4", true, owner, request1);
        request1.setItems(List.of(item4));
        ItemRequest request2 = new ItemRequest(2, user2, "request_description2", LocalDateTime.of(2022,8, 20, 9, 0), null);
        ItemRequest request3 = new ItemRequest(3, user2, "request_description3", LocalDateTime.of(2022,8, 20, 9, 0), null);
        assertEquals(List.of(request1, request2, request3), itemRequestRepository.findAll());
    }

    @Test
    void findOthersRequestsWithPage() {
        User user2 = new User(3, "User3", "email3@email.com");
        ItemRequest request3 = new ItemRequest(3, user2, "request_description3", LocalDateTime.of(2022,8, 20, 9, 0), null);
        Pageable page = PageRequest.of(0, 1, Sort.by("created").descending());
        assertEquals(List.of(request3), itemRequestRepository.findOthersRequests(1, page).getContent());
    }

    @Test
    void findOthersRequestsWithoutPage() {
        User owner = new User(1, "User1", "email1@email.com");
        User user1 = new User(2, "User2", "email2@email.com");
        User user2 = new User(3, "User3", "email3@email.com");
        ItemRequest request1 = new ItemRequest(1, user1, "request_description", LocalDateTime.of(2022,7, 20, 9, 0), null);
        Item item4 = new Item(4, "Item4", "description4", true, owner, request1);
        request1.setItems(List.of(item4));
        ItemRequest request2 = new ItemRequest(2, user2, "request_description2", LocalDateTime.of(2022,8, 20, 9, 0), null);
        ItemRequest request3 = new ItemRequest(3, user2, "request_description3", LocalDateTime.of(2022,8, 20, 9, 0), null);
        assertEquals(List.of(request3, request2, request1), itemRequestRepository.findOthersRequests(1));
    }

}