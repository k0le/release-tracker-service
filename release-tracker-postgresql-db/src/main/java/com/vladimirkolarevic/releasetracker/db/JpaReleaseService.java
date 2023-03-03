package com.vladimirkolarevic.releasetracker.db;

import com.vladimirkolarevic.releasetracker.domain.Release;
import com.vladimirkolarevic.releasetracker.domain.ReleaseService;
import com.vladimirkolarevic.releasetracker.domain.exception.NonExistentReleaseException;
import com.vladimirkolarevic.releasetracker.domain.exception.ReleaseTrackerException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JpaReleaseService implements ReleaseService {

    private final ReleaseJpaRepository releaseJpaRepository;

    private final ReleaseMapper mapper = Mappers.getMapper(ReleaseMapper.class);

    public JpaReleaseService(ReleaseJpaRepository releaseJpaRepository) {
        this.releaseJpaRepository = releaseJpaRepository;
    }

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
    public List<Release> list() {
        return releaseJpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(UUID uuid) {
        releaseJpaRepository
                .findByUuid(uuid)
                .map(ReleaseJpaEntity::getId)
                .ifPresentOrElse(this::deleteById, () -> {
                            throw new NonExistentReleaseException(String.format("Release with uuid: %s is not found", uuid));
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
            throw new ReleaseTrackerException(String.format("Release with id: %s cannot be deleted", id));
        }
    }
}
