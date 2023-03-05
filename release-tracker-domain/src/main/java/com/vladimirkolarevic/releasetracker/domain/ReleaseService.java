package com.vladimirkolarevic.releasetracker.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReleaseService {

    Release save(Release release);

    Release get(UUID id);

    List<Release> list(String name,
                       String description,
                       ReleaseStatus status,
                       LocalDate releaseDate,
                       LocalDateTime createdAt,
                       LocalDateTime lastUpdateAt);

    void delete(UUID uuid);

    Release update(Release release);

    Long count();

}
