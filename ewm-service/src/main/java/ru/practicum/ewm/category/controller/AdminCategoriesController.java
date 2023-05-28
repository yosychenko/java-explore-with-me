package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import javax.validation.Valid;

/**
 * API для работы с категориями
 */
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {
    @PostMapping
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return null;
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable long catId) {

    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(
            @PathVariable long catId,
            @Valid @RequestBody CategoryDto categoryDto
    ) {
        return null;
    }
}
