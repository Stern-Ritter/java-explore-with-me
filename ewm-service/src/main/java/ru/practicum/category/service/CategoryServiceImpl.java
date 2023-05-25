package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CreateCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.utils.Templates.CATEGORY_NOT_EXISTS_TEMPLATE;
import static ru.practicum.utils.Utils.calculateFirstPageNumber;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final Sort.TypedSort<Category> categorySort = Sort.sort(Category.class);

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public CategoryDto getById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_EXISTS_TEMPLATE, categoryId)));

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<CategoryDto> getAll(Integer offset, Integer limit) {
        Sort sortByIdAsc = categorySort.by(Category::getId).ascending();
        Pageable pageable = PageRequest.of(calculateFirstPageNumber(offset, limit), limit, sortByIdAsc);

        return categoryRepository.findCategories(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CategoryDto create(CreateCategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);

        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CategoryDto update(Long categoryId, UpdateCategoryDto categoryDto) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_EXISTS_TEMPLATE, categoryId)));

        Category patchedCategory = CategoryMapper.toCategory(categoryDto);
        Category category = CategoryMapper.mergePatchedCategory(savedCategory, patchedCategory);

        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_EXISTS_TEMPLATE, categoryId)));

        categoryRepository.deleteById(categoryId);
    }
}
