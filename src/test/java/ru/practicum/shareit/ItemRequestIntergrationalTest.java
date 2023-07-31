package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class ItemRequestIntergrationalTest {

    private final ItemRequestService itemRequestService;

    @Test
    void findOthersRequestsWithPage() {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(2)
                .description("request_description2")
                .created(LocalDateTime.of(2022, 8, 20, 9, 0))
                .items(new ArrayList<>())
                .build();
        Assertions.assertEquals(List.of(dto), itemRequestService.findOthersRequests(2, 1, 1),
                "Ошибка при поиске запросов от других пользователей с пагинацией.");
    }

    @Test
    void findOthersRequestsWithoutPage() {
        ItemRequestDto dto1 = ItemRequestDto.builder()
                .id(2)
                .description("request_description2")
                .created(LocalDateTime.of(2022, 8, 20, 9, 0))
                .items(new ArrayList<>())
                .build();
        ItemRequestDto dto2 = ItemRequestDto.builder()
                .id(3)
                .description("request_description3")
                .created(LocalDateTime.of(2022, 9, 20, 9, 0))
                .items(new ArrayList<>())
                .build();
        Assertions.assertEquals(List.of(dto2, dto1), itemRequestService.findOthersRequests(2, null, 1),
                "Ошибка при поиске запросов от других пользователей без пагинации.");
    }

    @Test
    void save() {
        ItemRequestDto fromDto = ItemRequestDto.builder()
                .description("description4")
                .build();
        ItemRequestDto request = itemRequestService.save(fromDto, 3);
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(4)
                .description("description4")
                .created(request.getCreated())
                .build();
        Assertions.assertEquals(dto, request);
    }

}