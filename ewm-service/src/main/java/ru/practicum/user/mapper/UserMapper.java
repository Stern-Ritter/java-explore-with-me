package ru.practicum.user.mapper;

import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserFullDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;
import ru.practicum.user.view.UserFullView;
import ru.practicum.user.view.UserShortView;

import static ru.practicum.utils.Utils.calculateRating;

public class UserMapper {
    public static User toUser(CreateUserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserFullDto toUserFullDto(User user) {
        UserFullDto userDto = new UserFullDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRating(calculateRating(user.getEvents()));
        return userDto;
    }

    public static UserFullDto toUserFullDto(UserFullView userView) {
        UserFullDto userDto = new UserFullDto();
        userDto.setId(userView.getId());
        userDto.setName(userView.getName());
        userDto.setEmail(userView.getEmail());
        userDto.setRating(userView.getRating());
        return userDto;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
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
