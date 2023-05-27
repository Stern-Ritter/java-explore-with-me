package ru.practicum.category.mapper;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CreateCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.view.CategoryView;

public class CategoryMapper {
    public static Category toCategory(CreateCategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    public static Category toCategory(UpdateCategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static CategoryDto toCategoryDto(CategoryView categoryView) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryView.getId());
        categoryDto.setName(categoryView.getName());
        return categoryDto;
    }

    public static Category mergePatchedCategory(Category savedCategory, Category patchedCategory) {
        Category category = new Category();
        category.setId(savedCategory.getId());
        category.setName(patchedCategory.getName());
        return category;
    }
}
