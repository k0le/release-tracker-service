package com.vladimirkolarevic.releasetracker.db;

import com.vladimirkolarevic.releasetracker.domain.Release;
import com.vladimirkolarevic.releasetracker.domain.ReleaseService;
import com.vladimirkolarevic.releasetracker.domain.ReleaseStatus;
import com.vladimirkolarevic.releasetracker.domain.exception.NonExistentReleaseException;
import com.vladimirkolarevic.releasetracker.domain.exception.ReleaseTrackerException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class JpaReleaseService implements ReleaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaReleaseService.class);
    private final ReleaseJpaRepository releaseJpaRepository;
    private final CriteriaRepository criteriaRepository;

    private final ReleaseMapper mapper = Mappers.getMapper(ReleaseMapper.class);

    public JpaReleaseService(ReleaseJpaRepository releaseJpaRepository, CriteriaRepository criteriaRepository) {
        this.releaseJpaRepository = releaseJpaRepository;
        this.criteriaRepository = criteriaRepository;
    }

    @Transactional
    @Override
    public Release save(Release release) {
        LOGGER.info("Release is saving {}",release);
        var releaseJpaToSave = mapper.fromDomain(release);
        var savedReleaseJpa = releaseJpaRepository.save(releaseJpaToSave);
        LOGGER.info("Release saved {}",release);
        return mapper.toDomain(savedReleaseJpa);
    }

    @Override
    public Release get(UUID uuid) {
        LOGGER.info("Retrieving release with uuid: ",uuid);
        return releaseJpaRepository
            .findByUuid(uuid)
            .map(mapper::toDomain)
            .orElseThrow(() -> new NonExistentReleaseException(String
                .format("Release with uuid: %s is not found", uuid)));
    }

    @Override
    public List<Release> list(String name,
                              String description,
                              ReleaseStatus status,
                              LocalDate releaseDate,
                              LocalDateTime createdAt,
                              LocalDateTime lastUpdateAt) {
        LOGGER.info("Filtering releases");
        return criteriaRepository.filter(name,description,status,releaseDate,createdAt,lastUpdateAt).stream().map(mapper::toDomain).toList();
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        LOGGER.info("Deleting release with uuid:{}",uuid);
        releaseJpaRepository
            .findByUuid(uuid)
            .map(ReleaseJpaEntity::getId)
            .ifPresentOrElse(this::deleteById, () -> {
                LOGGER.warn("Release not found for uuid: {}",uuid);
                throw new NonExistentReleaseException(String.format("Release with uuid: %s is not found", uuid));
            });


    }

    @Transactional
    @Override
    public Release update(Release release) {

            return releaseJpaRepository.findByUuid(release.id()).map(releaseJpaEntity -> {
                releaseJpaEntity.setName(release.name());
                releaseJpaEntity.setDescription(release.description());
                releaseJpaEntity.setReleaseDate(release.releaseDate());
                var releaseStatusJpaEntity =
                    release.status() != null ? ReleaseStatusJpaEntity.valueOf(release.status().name()) : null;
                releaseJpaEntity.setStatus(releaseStatusJpaEntity);
                releaseJpaEntity.setLastUpdateAt(release.lastUpdateAt());
                releaseJpaEntity.setCreatedAt(release.createdAt());
                return releaseJpaEntity;
            }).map(mapper::toDomain).orElseThrow(()->{
                LOGGER.warn("Release not found for uuid: {}",release.id());
                throw new NonExistentReleaseException(String.format("Release with uuid: %s is not found", release.id()));
            });


    }

    @Override
    public Long count() {
        return releaseJpaRepository.count();
    }

    private void deleteById(Long id) {
        try {
            releaseJpaRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("Error while deleting release with id: {}",id);
            throw new ReleaseTrackerException(String.format("Release with id: %s cannot be deleted", id));
        }
    }
}
