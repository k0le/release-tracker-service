package com.vladimirkolarevic.releasetracker.db;

import com.vladimirkolarevic.releasetracker.domain.Release;
import com.vladimirkolarevic.releasetracker.domain.ReleaseStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CriteriaRepository {

    List<ReleaseJpaEntity> filter(String name,
                         String description,
                         ReleaseStatus status,
                         LocalDate releaseDate,
                         LocalDateTime createdAt,
                         LocalDateTime lastUpdateAt);
}
