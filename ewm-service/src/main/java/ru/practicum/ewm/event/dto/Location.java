package ru.practicum.ewm.event.dto;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

/**
 * Широта и долгота места проведения события
 */
@Data
@NotBlank
@Embeddable
public class Location {
    private float lat;
    private float lon;
}
