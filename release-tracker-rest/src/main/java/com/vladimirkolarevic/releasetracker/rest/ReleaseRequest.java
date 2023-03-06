package com.vladimirkolarevic.releasetracker.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ReleaseRequest(

    @NotBlank(message = "name cannot be blank")
    String name,

    @NotEmpty(message = "description cannot be blank")
    String description,

    @NotNull(message = "status cannot be null")
    ReleaseResponseStatus status,

    @NotBlank(message = "releaseDate cannot be blank")
    String releaseDate
) {
}
