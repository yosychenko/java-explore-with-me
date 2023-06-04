package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.storage.CompilationStorage;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {
    private final CompilationStorage compilationStorage;
    private final EventService eventService;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        if (pinned == null) {
            return compilationStorage.findAll(pageable).stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }
        return compilationStorage.findAllByPinned(pinned, pageable).stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        Optional<Compilation> compilation = compilationStorage.findById(compId);
        if (compilation.isPresent()) {
            return CompilationMapper.toCompilationDto(compilation.get());
        } else {
            throw new EntityNotFoundException(String.format("Подборка событий с ID=%s не найдена", compId));
        }
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<EventFullDto> eventsDtos = eventService.getEventsByIds(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.fromNewCompilationDto(newCompilationDto, eventsDtos);

        return CompilationMapper.toCompilationDto(compilationStorage.save(compilation));
    }

    @Override
    public void deleteCompilation(long compId) {
        compilationStorage.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        // TODO: Брать подборку из БД
        List<EventFullDto> eventsDtos = eventService.getEventsByIds(updateCompilationRequest.getEvents());
        Compilation compilation = CompilationMapper.fromCompilationDto(getCompilationById(compId), eventsDtos);

        return CompilationMapper.toCompilationDto(compilationStorage.save(compilation));
    }
}
