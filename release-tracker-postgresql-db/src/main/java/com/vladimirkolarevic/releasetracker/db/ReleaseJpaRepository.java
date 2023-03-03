package com.vladimirkolarevic.releasetracker.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


interface ReleaseJpaRepository extends JpaRepository<ReleaseJpaEntity,Long> {

    Optional<ReleaseJpaEntity>findByUuid(UUID uuid);
}
