package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CreateCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAll(Boolean pinned, Integer offset, Integer limit);

    CompilationDto getById(Long compilationId);

    CompilationDto create(CreateCompilationDto compilationDto);

    CompilationDto update(UpdateCompilationDto compilationDto, Long compilationId);

    void delete(Long compilationId);
}
