package com.vladimirkolarevic.releasetracker.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

 record Release(
        String id,
        String name,
        String description,
        ReleaseStatus status,
        LocalDate releaseDate,
        LocalDateTime createdAt,
        LocalDateTime lastUpdateAt

) {
    enum ReleaseStatus {
        CREATED, IN_DEVELOPMENT,ON_DEV,QA_DONE_ON_DEV,ON_STAGING,QA_DONE_ON_STAGING,ON_PROD,DONE

    }
}
