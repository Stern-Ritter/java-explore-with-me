package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static ru.practicum.utils.Templates.CATEGORY_EMPTY_REQUEST_BODY_EXCEPTION;
import static ru.practicum.utils.Templates.CATEGORY_NAME_VALIDATION_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@NotNull(message = CATEGORY_EMPTY_REQUEST_BODY_EXCEPTION)
public class CreateCategoryDto {
    @NotEmpty(message = CATEGORY_NAME_VALIDATION_EXCEPTION)
    private String name;
}
