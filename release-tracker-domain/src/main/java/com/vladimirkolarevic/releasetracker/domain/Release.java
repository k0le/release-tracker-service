package com.vladimirkolarevic.releasetracker.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

 public record Release(
        String id,
        String name,
        String description,
        ReleaseStatus status,
        LocalDate releaseDate,
        LocalDateTime createdAt,
        LocalDateTime lastUpdateAt

) {

}
