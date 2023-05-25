package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static ru.practicum.utils.Templates.COMPILATION_EMPTY_REQUEST_BODY_EXCEPTION;
import static ru.practicum.utils.Templates.COMPILATION_EMPTY_TITLE_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.COMPILATION_TITLE_LENGTH_VALIDATION_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@NotNull(message = COMPILATION_EMPTY_REQUEST_BODY_EXCEPTION)
public class CreateCompilationDto {
    @NotBlank(message = COMPILATION_EMPTY_TITLE_VALIDATION_EXCEPTION)
    @Size(message = COMPILATION_TITLE_LENGTH_VALIDATION_EXCEPTION, min = 2, max = 50)
    private String title;

    private Boolean pinned;

    private List<Long> events;
}
