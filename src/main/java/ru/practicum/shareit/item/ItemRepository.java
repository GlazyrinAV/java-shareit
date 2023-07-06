package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("from Item it where it.owner.id in :ownerId")
    Collection<Item> findAllWhereOwnerIdIn(Integer ownerId);

    @Query("from Item it where it.available = TRUE and " +
            "(lower(it.name) like lower(concat('%', :text, '%')) or " +
            "lower(it.description) like lower(concat('%', :text, '%'))) ")
    Collection<Item> findByName(String text);

}