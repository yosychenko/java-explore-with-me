package ru.practicum.ewm.event.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Широта и долгота места проведения события
 */
@Data
@NotBlank
public class Location {
    private float lat;
    private float lon;
}
