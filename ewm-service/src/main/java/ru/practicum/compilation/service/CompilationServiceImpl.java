package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CreateCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.view.CompilationView;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.utils.Templates.COMPILATION_NOT_EXISTS_TEMPLATE;
import static ru.practicum.utils.Utils.calculateFirstPageNumber;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final Sort.TypedSort<CompilationView> compilationSort = Sort.sort(CompilationView.class);

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<CompilationDto> getAll(Boolean pinned, Integer offset, Integer limit) {
        Sort sortByIdAsc = compilationSort.by(CompilationView::getId).ascending();
        Pageable pageable = PageRequest.of(calculateFirstPageNumber(offset, limit), limit, sortByIdAsc);
        return compilationRepository.findAllByPinned(pinned, pageable).stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public CompilationDto getById(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(String.format(COMPILATION_NOT_EXISTS_TEMPLATE, compilationId)));

        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CompilationDto create(CreateCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);

        List<Long> eventsId = compilationDto.getEvents();
        if (eventsId != null) {
            List<Event> events = eventRepository.findAllByIdIn(eventsId);
            compilation.setEvents(events);
        }

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CompilationDto update(UpdateCompilationDto compilationDto, Long compilationId) {
        Compilation savedCompilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(String.format(COMPILATION_NOT_EXISTS_TEMPLATE, compilationId)));

        Compilation patchedCompilation = CompilationMapper.toCompilation(compilationDto);
        Compilation compilation = CompilationMapper.mergePatchedCompilation(savedCompilation, patchedCompilation);

        List<Long> eventsId = compilationDto.getEvents();
        if (eventsId != null) {
            List<Event> events = eventRepository.findAllByIdIn(eventsId);
            compilation.setEvents(events);
        }

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(Long compilationId) {
        compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(String.format(COMPILATION_NOT_EXISTS_TEMPLATE, compilationId)));

        compilationRepository.deleteById(compilationId);
    }
}
