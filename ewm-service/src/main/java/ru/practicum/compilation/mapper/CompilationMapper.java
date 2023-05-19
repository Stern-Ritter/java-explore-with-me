package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CreateCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.view.CompilationView;
import ru.practicum.event.mapper.EventMapper;

import java.util.stream.Collectors;

import static ru.practicum.utils.Utils.coalesce;

public class CompilationMapper {
    public static Compilation toCompilation(CreateCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.getPinned());
        return compilation;
    }

    public static Compilation toCompilation(UpdateCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.getPinned());
        return compilation;
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setEvents(compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()));
        return compilationDto;
    }

    public static CompilationDto toCompilationDto(CompilationView compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setEvents(compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()));
        return compilationDto;
    }

    public static Compilation mergePatchedCompilation(Compilation savedCompilation, Compilation patchedCompilation) {
        Compilation compilation = new Compilation();
        compilation.setId(savedCompilation.getId());
        compilation.setTitle(coalesce(patchedCompilation.getTitle(), savedCompilation.getTitle()));
        compilation.setPinned(coalesce(patchedCompilation.getPinned(), savedCompilation.getPinned()));
        return compilation;
    }
}
