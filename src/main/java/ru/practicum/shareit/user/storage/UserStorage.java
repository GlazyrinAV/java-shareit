package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

public interface UserStorage extends JpaRepository<User, Integer> {

//    User saveNew(User user);
//
//    Collection<User> findAll();
//
//    User findById(int id);
//
//    void removeById(int id);
//
//    User updateById(int id, User user);
//
//    boolean isExists(int userId);

}