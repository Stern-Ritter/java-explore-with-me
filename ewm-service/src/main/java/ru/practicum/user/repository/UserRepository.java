package ru.practicum.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;
import ru.practicum.user.view.UserFullView;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT " +
            "u FROM User u " +
            "WHERE ((:ids) is null OR u.id IN (:ids))")
    List<UserFullView> findUsers(List<Long> ids, Pageable pageable);
}
