package com.vladimirkolarevic.releasetracker.db;

import com.vladimirkolarevic.releasetracker.domain.Release;
import com.vladimirkolarevic.releasetracker.domain.ReleaseStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

class ReleaseHelper {

    public static Release createRelease(final UUID uuid, final String name, final String description,
                                        final ReleaseStatus status, final LocalDate releaseDate,
                                        final LocalDateTime createdAt, final LocalDateTime lastUpdateAt) {
        return new Release(uuid, name, description, status, releaseDate, createdAt, lastUpdateAt);
    }

    public static ReleaseJpaEntity createReleaseJpaEntity(Long id, UUID uuid, String name, String description,
                                                          ReleaseStatusJpaEntity status, LocalDate releaseDate,
                                                          LocalDateTime createdAt, LocalDateTime lastUpdateAt) {
        var releaseJpaEntity = new ReleaseJpaEntity();
        releaseJpaEntity.setUuid(uuid);
        releaseJpaEntity.setName(name);
        releaseJpaEntity.setDescription(description);
        releaseJpaEntity.setStatus(status);
        releaseJpaEntity.setReleaseDate(releaseDate);
        releaseJpaEntity.setCreatedAt(createdAt);
        releaseJpaEntity.setLastUpdateAt(lastUpdateAt);
        releaseJpaEntity.setId(id);
        return releaseJpaEntity;
    }

    public static ReleaseJpaEntity createReleaseJpaEntityRandomId(UUID uuid, String name, String description,
                                                                  ReleaseStatusJpaEntity status, LocalDate releaseDate,
                                                                  LocalDateTime createdAt, LocalDateTime lastUpdateAt) {
        var random = new Random();
        return createReleaseJpaEntity(random.nextLong(), uuid, name, description, status, releaseDate, createdAt,
            lastUpdateAt);
    }
}
