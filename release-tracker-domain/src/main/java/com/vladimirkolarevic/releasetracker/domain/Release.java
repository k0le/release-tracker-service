package com.vladimirkolarevic.releasetracker.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Release(
        UUID id,
        String name,
        String description,
        ReleaseStatus status,
        LocalDate releaseDate,
        LocalDateTime createdAt,
        LocalDateTime lastUpdateAt

) {

}
