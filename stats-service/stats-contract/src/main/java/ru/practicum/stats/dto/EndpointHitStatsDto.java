package ru.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@AllArgsConstructor
public class EndpointHitStatsDto {
    @NotBlank String app;
    @NotBlank String uri;
    Long hits;
}
