package com.vladimirkolarevic.releasetracker.domain;

import com.vladimirkolarevic.releasetracker.domain.exception.ReleaseTrackerException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReleaseService {

    Release save(Release release) throws ReleaseTrackerException;

    Release get(UUID id) throws ReleaseTrackerException;

    List<Release> list(String name,
                       String description,
                       ReleaseStatus status,
                       LocalDate releaseDate,
                       LocalDateTime createdAt,
                       LocalDateTime lastUpdateAt);

    void delete(UUID uuid) throws ReleaseTrackerException;

    Release update(Release release) throws ReleaseTrackerException;

    Long count();

}
