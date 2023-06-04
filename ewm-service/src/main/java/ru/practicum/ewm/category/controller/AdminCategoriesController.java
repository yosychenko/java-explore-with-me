package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.service.CategoriesService;

import javax.validation.Valid;

/**
 * API для работы с категориями
 */
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {
    private final CategoriesService categoriesService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoriesService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        // TODO: добавить проверку на связь с любым event, если есть - не удалять событие, ошибка 409
        categoriesService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(
            @PathVariable long catId,
            @Valid @RequestBody CategoryDto categoryDto
    ) {
        return categoriesService.updateCategory(catId, categoryDto);
    }
}
