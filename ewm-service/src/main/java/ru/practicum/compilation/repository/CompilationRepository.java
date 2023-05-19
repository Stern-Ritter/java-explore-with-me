package ru.practicum.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.view.CompilationView;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<CompilationView> findAllByPinned(Boolean pinned, Pageable pageable);
}
