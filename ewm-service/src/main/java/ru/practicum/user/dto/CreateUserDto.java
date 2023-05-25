package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static ru.practicum.utils.Templates.USER_EMAIL_LENGTH_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.USER_EMAIL_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.USER_EMPTY_EMAIL_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.USER_EMPTY_NAME_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.USER_EMPTY_REQUEST_BODY_EXCEPTION;
import static ru.practicum.utils.Templates.USER_NAME_LENGTH_VALIDATION_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@NotNull(message = USER_EMPTY_REQUEST_BODY_EXCEPTION)
public class CreateUserDto {
    @NotBlank(message = USER_EMPTY_NAME_VALIDATION_EXCEPTION)
    @Size(message = USER_NAME_LENGTH_VALIDATION_EXCEPTION, min = 2, max = 250)
    private String name;
    @NotBlank(message = USER_EMPTY_EMAIL_VALIDATION_EXCEPTION)
    @Size(message = USER_EMAIL_LENGTH_VALIDATION_EXCEPTION, min = 6, max = 254)
    @Email(message = USER_EMAIL_VALIDATION_EXCEPTION)
    private String email;
}
