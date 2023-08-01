package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    @Query("FROM ItemRequest IR WHERE IR.owner.id IN :ownerId ORDER BY IR.created DESC")
    Collection<ItemRequest> findAll(int ownerId);

    @Query("FROM ItemRequest IR WHERE IR.owner.id NOT IN :userId ")
    Page<ItemRequest> findOthersRequests(int userId, Pageable pageable);

}