package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.storage.CompilationStorage;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    @Transactional
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
    @Transactional
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilationToUpdate = CompilationMapper.fromCompilationDto(getCompilationById(compId));

        if (updateCompilationRequest.getEvents() != null) {
            Set<Event> events = eventService.getEventsByIds(updateCompilationRequest.getEvents()).stream()
                    .map(EventMapper::fromEventFullDto)
                    .collect(Collectors.toSet());

            if (events.isEmpty()) {
                throw new EntityNotFoundException("Указанные события не найдены.");
            }

            compilationToUpdate.setEvents(events);
        }

        if (updateCompilationRequest.getTitle() != null) {
            compilationToUpdate.setTitle(updateCompilationRequest.getTitle());
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilationToUpdate.setPinned(updateCompilationRequest.getPinned());
        }

        return CompilationMapper.toCompilationDto(compilationStorage.save(compilationToUpdate));
    }
}
