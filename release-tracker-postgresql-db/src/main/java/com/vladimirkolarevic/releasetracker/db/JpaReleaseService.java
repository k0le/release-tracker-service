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
import org.springframework.stereotype.Service;

@Service
class JpaReleaseService implements ReleaseService {

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
        var releaseJpaToSave = mapper.fromDomain(release);
        var savedReleaseJpa = releaseJpaRepository.save(releaseJpaToSave);
        return mapper.toDomain(savedReleaseJpa);
    }

    @Override
    public Release get(UUID uuid) {
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
        return criteriaRepository.filter(name,description,status,releaseDate,createdAt,lastUpdateAt).stream().map(mapper::toDomain).toList();
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        releaseJpaRepository
            .findByUuid(uuid)
            .map(ReleaseJpaEntity::getId)
            .ifPresentOrElse(this::deleteById, () -> {
                throw new NonExistentReleaseException(String.format("Release with uuid: %s is not found", uuid));
            });


    }

    @Transactional
    @Override
    public Release update(Release release) throws ReleaseTrackerException {
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
        }).map(mapper::toDomain).get();
    }

    @Override
    public Long count() {
        return releaseJpaRepository.count();
    }

    private void deleteById(Long id) {
        try {
            releaseJpaRepository.deleteById(id);
        } catch (Exception e) {
            throw new ReleaseTrackerException(String.format("Release with id: %s cannot be deleted", id));
        }
    }
}
