package com.vladimirkolarevic.releasetracker.db;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


interface ReleaseJpaRepository extends JpaRepository<ReleaseJpaEntity, Long> {

    Optional<ReleaseJpaEntity> findByUuid(UUID uuid);
}
