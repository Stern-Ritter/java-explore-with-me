package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.view.RequestView;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<RequestView> findAllByRequesterId(Long userId);

    Optional<List<RequestView>> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);
}
