package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.view.EventFullView;
import ru.practicum.event.view.EventShortView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<EventFullView> findByIdAndState(Long eventId, EventState state);

    Optional<EventFullView> findByIdAndInitiatorId(Long eventId, Long userId);

    @Query("SELECT e FROM Event e " +
            "WHERE (:text IS NULL OR (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')))) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (CAST(:start AS timestamp) IS NULL OR e.eventDate >= :start) " +
            "AND (CAST(:end AS timestamp) IS NULL OR e.eventDate <= :end) " +
            "ORDER BY e.id"
    )
    List<EventFullView> findAllWithFilters(@Param("text") String text,
                                           @Param("categories") List<Long> categories,
                                           @Param("paid") Boolean paid,
                                           @Param("start") LocalDateTime rangeStart,
                                           @Param("end") LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND ((:states) IS NULL OR e.state IN (:states)) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (CAST(:start AS timestamp) IS NULL OR e.eventDate >= :start) " +
            "AND (CAST(:end AS timestamp) IS NULL OR e.eventDate <= :end) " +
            "ORDER BY e.id"
    )
    List<EventFullView> findAllWithAdminFilters(@Param("users") List<Long> users,
                                                @Param("states") List<EventState> states,
                                                @Param("categories") List<Long> categories,
                                                @Param("start") LocalDateTime rangeStart,
                                                @Param("end") LocalDateTime rangeEnd, Pageable pageable);

    List<EventShortView> findAllByInitiatorId(Long userId, Pageable pageable);

    List<Event> findAllByIdIn(List<Long> events);
}
