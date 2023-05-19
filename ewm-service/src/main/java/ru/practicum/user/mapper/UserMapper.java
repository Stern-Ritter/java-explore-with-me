package ru.practicum.user.mapper;

import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;
import ru.practicum.user.view.UserShortView;
import ru.practicum.user.view.UserView;

public class UserMapper {
    public static User toUser(CreateUserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static UserDto toUserDto(UserView userView) {
        UserDto userDto = new UserDto();
        userDto.setId(userView.getId());
        userDto.setName(userView.getName());
        userDto.setEmail(userView.getEmail());
        return userDto;
    }

    public static UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

    public static UserShortDto toUserShortDto(UserShortView userView) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(userView.getId());
        userShortDto.setName(userView.getName());
        return userShortDto;
    }
}
