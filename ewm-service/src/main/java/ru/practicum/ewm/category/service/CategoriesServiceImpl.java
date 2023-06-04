package ru.practicum.ewm.category.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.storage.CategoryStorage;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.DuplicateEntityException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.EventsOfDeletedCategoryExistException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoryStorage categoryStorage;
    private final EventService eventService;

    @Autowired
    public CategoriesServiceImpl(CategoryStorage categoryStorage, @Lazy EventService eventService) {
        this.categoryStorage = categoryStorage;
        this.eventService = eventService;
    }

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.fromNewCategoryDto(newCategoryDto);
        try {
            return CategoryMapper.toCategoryDto(categoryStorage.save(category));
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEntityException(String.format("Категория c name=%s уже существует.", category.getName()));
        }
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        CategoryDto categoryToDelete = getCategoryById(catId);
        List<EventFullDto> eventsOfThisCategory = eventService.getEventsByCategoryId(categoryToDelete.getId());
        if (eventsOfThisCategory.isEmpty()) {
            categoryStorage.deleteById(categoryToDelete.getId());
        } else {
            throw new EventsOfDeletedCategoryExistException(catId);
        }
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        Category categoryToUpdate = CategoryMapper.fromCategoryDto(getCategoryById(catId));
        categoryToUpdate.setName(categoryDto.getName());
        try {
            return CategoryMapper.toCategoryDto(categoryStorage.save(categoryToUpdate));
        } catch (DataIntegrityViolationException ex) {
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
