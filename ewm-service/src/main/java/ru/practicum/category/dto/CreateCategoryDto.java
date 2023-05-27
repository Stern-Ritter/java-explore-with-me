package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static ru.practicum.utils.Templates.CATEGORY_EMPTY_REQUEST_BODY_EXCEPTION;
import static ru.practicum.utils.Templates.CATEGORY_NAME_LENGTH_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.CATEGORY_NAME_VALIDATION_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@NotNull(message = CATEGORY_EMPTY_REQUEST_BODY_EXCEPTION)
public class CreateCategoryDto {
    @NotBlank(message = CATEGORY_NAME_VALIDATION_EXCEPTION)
    @Size(message = CATEGORY_NAME_LENGTH_VALIDATION_EXCEPTION, min = 2, max = 50)
    private String name;
}
