package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.storage.CategoryStorage;
import ru.practicum.ewm.exception.DuplicateEntityException;
import ru.practicum.ewm.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoryStorage categoryStorage;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.fromNewCategoryDto(newCategoryDto);
        try {
            return CategoryMapper.toCategoryDto(categoryStorage.save(category));
        } catch (DuplicateKeyException ex) {
            throw new DuplicateEntityException(String.format("Категория c name=%s уже существует.", category.getName()));
        }
    }

    @Override
    public void deleteCategory(long catId) {
        CategoryDto categoryToDelete = getCategoryById(catId);
        categoryStorage.deleteById(categoryToDelete.getId());
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        getCategoryById(catId);
        Category updatedCategory = CategoryMapper.fromCategoryDto(categoryDto);
        try {
            return CategoryMapper.toCategoryDto(categoryStorage.save(updatedCategory));
        } catch (DuplicateKeyException ex) {
            throw new DuplicateEntityException(String.format("Категория c name=%s уже существует.", categoryDto.getName()));
        }
    }

    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        return categoryStorage.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        Optional<Category> category = categoryStorage.findById(catId);
        if (category.isPresent()) {
            return CategoryMapper.toCategoryDto(category.get());
        } else {
            throw new EntityNotFoundException(String.format("Категория c ID=%s не найдена.", catId));
        }
    }
}
