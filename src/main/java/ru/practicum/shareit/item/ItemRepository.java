package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("select it from Item it where it.owner.id in ?1")
    Collection<Item> findAllWhereOwnerIdIn(Integer ownerId);

    @Query("select it from Item it where it.available = TRUE and " +
            "(lower(it.name) like lower(concat('%', ?1, '%')) or " +
            "lower(it.description) like lower(concat('%', ?1, '%'))) ")
    Collection<Item> findByName(String text);

}