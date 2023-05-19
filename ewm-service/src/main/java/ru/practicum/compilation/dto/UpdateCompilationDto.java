package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.practicum.utils.Templates.COMPILATION_EMPTY_REQUEST_BODY_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@NotNull(message = COMPILATION_EMPTY_REQUEST_BODY_EXCEPTION)
public class UpdateCompilationDto {
    private String title;

    private List<Long> events;

    private Boolean pinned;
}
