package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Collection<Comment> findByItem_Id(int itemId);

}
