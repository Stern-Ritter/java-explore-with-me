package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CreateCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto getById(Long categoryId);

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto create(CreateCategoryDto categoryDto);

    CategoryDto update(Long categoryId, UpdateCategoryDto categoryDto);

    void delete(Long categoryId);
}
