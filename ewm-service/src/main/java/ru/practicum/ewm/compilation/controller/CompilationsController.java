package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationsService;

import java.util.List;

/**
 * Публичный API для работы с подборками событий
 */
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationsController {
    private final CompilationsService compilationsService;

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return compilationsService.getCompilations(pinned, PageRequest.of(from, size));
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        return compilationsService.getCompilationById(compId);
    }

}
