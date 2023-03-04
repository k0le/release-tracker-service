package com.vladimirkolarevic.releasetracker.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import org.springframework.http.HttpStatus;

public record ReleaseResponse(
    UUID id,
    String name,
    String description,
    ReleaseResponseStatus status,
    String releaseDate,
    String createdAt,
    String lastUpdateAt,
    @JsonIgnore
    HttpStatus httpStatus
) {

    public ReleaseResponse withStatusCode(HttpStatus status) {
        return new ReleaseResponse(id(), name(), description(), status(), releaseDate(), createdAt(), lastUpdateAt(),
            status);
    }

}
