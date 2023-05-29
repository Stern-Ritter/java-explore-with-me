package ru.practicum.user.service;

import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserFullDto;

import java.util.List;

public interface UserService {
    List<UserFullDto> getAll(List<Long> ids, Integer from, Integer size);

    UserDto create(CreateUserDto userDto);

    void delete(Long userId);
}
