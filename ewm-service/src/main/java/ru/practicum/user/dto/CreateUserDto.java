package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static ru.practicum.utils.Templates.USER_EMAIL_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.USER_EMPTY_EMAIL_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.USER_EMPTY_NAME_VALIDATION_EXCEPTION;
import static ru.practicum.utils.Templates.USER_EMPTY_REQUEST_BODY_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@NotNull(message = USER_EMPTY_REQUEST_BODY_EXCEPTION)
public class CreateUserDto {
    @NotEmpty(message = USER_EMPTY_NAME_VALIDATION_EXCEPTION)
    private String name;
    @NotEmpty(message = USER_EMPTY_EMAIL_VALIDATION_EXCEPTION)
    @Email(message = USER_EMAIL_VALIDATION_EXCEPTION)
    private String email;
}
