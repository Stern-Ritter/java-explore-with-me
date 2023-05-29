package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserFullDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.utils.Templates.USER_NOT_EXISTS_TEMPLATE;
import static ru.practicum.utils.Utils.calculateFirstPageNumber;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Sort.TypedSort<UserFullDto> userSort = Sort.sort(UserFullDto.class);

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<UserFullDto> getAll(List<Long> ids, Integer offset, Integer limit) {
        Sort sortByIdAsc = userSort.by(UserFullDto::getId).ascending();
        Pageable pageable = PageRequest.of(calculateFirstPageNumber(offset, limit), limit, sortByIdAsc);
        return userRepository.findUsers(ids, pageable).stream()
                .map(UserMapper::toUserFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserDto create(CreateUserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXISTS_TEMPLATE, userId)));
        userRepository.deleteById(userId);
    }
}
