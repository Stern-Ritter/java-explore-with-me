package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.practicum.utils.Templates.COMPILATION_EMPTY_REQUEST_BODY_EXCEPTION;
import static ru.practicum.utils.Templates.COMPILATION_EMPTY_TITLE_VALIDATION_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@NotNull(message = COMPILATION_EMPTY_REQUEST_BODY_EXCEPTION)
public class CreateCompilationDto {
    @NotEmpty(message = COMPILATION_EMPTY_TITLE_VALIDATION_EXCEPTION)
    private String title;

    private Boolean pinned;

    private List<Long> events;
}
