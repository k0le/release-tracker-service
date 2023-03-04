package com.vladimirkolarevic.releasetracker.db;

import static com.vladimirkolarevic.releasetracker.db.ReleaseHelper.createRelease;
import static com.vladimirkolarevic.releasetracker.db.ReleaseHelper.createReleaseJpaEntityRandomId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.vladimirkolarevic.releasetracker.domain.ReleaseStatus;
import com.vladimirkolarevic.releasetracker.domain.exception.NonExistentReleaseException;
import com.vladimirkolarevic.releasetracker.domain.exception.ReleaseTrackerException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JpaReleaseServiceTest {

    @Mock
    private ReleaseJpaRepository releaseJpaRepository;


    @InjectMocks
    private JpaReleaseService jpaReleaseService;


    @Test
    void givenReleaseToSave_returnSavedRelease() {
        var release = createRelease(
            UUID.randomUUID(),
            "Test release",
            "This is dummy test release",
            ReleaseStatus.CREATED,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());
        var releaseJpaSaved = createReleaseJpaEntityRandomId(
            release.id(),
            release.name(),
            release.description(),
            ReleaseStatusJpaEntity.CREATED,
            release.releaseDate(),
            release.createdAt(),
            release.lastUpdateAt());
        when(releaseJpaRepository.save(any())).thenReturn(releaseJpaSaved);
        var savedRelease = jpaReleaseService.save(release);

        assertThat(savedRelease)
            .hasFieldOrPropertyWithValue("id", release.id())
            .hasFieldOrPropertyWithValue("name", release.name())
            .hasFieldOrPropertyWithValue("description", release.description())
            .hasFieldOrPropertyWithValue("status", ReleaseStatus.CREATED)
            .hasFieldOrPropertyWithValue("releaseDate", release.releaseDate())
            .hasFieldOrPropertyWithValue("createdAt", release.createdAt())
            .hasFieldOrPropertyWithValue("lastUpdateAt", release.lastUpdateAt());

    }

    @Test
    void givenId_returnRelease() {
        var uuid = UUID.randomUUID();
        var releaseJpaEntity = createReleaseJpaEntityRandomId(
            uuid,
            "Test release",
            "This is dummy test release",
            ReleaseStatusJpaEntity.CREATED,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());
        when(releaseJpaRepository.findByUuid(uuid)).thenReturn(Optional.of(releaseJpaEntity));
        var release = jpaReleaseService.get(uuid);

        assertThat(release)
            .hasFieldOrPropertyWithValue("id", releaseJpaEntity.getUuid())
            .hasFieldOrPropertyWithValue("name", releaseJpaEntity.getName())
            .hasFieldOrPropertyWithValue("description", releaseJpaEntity.getDescription())
            .hasFieldOrPropertyWithValue("status", ReleaseStatus.CREATED)
            .hasFieldOrPropertyWithValue("releaseDate", releaseJpaEntity.getReleaseDate())
            .hasFieldOrPropertyWithValue("createdAt", releaseJpaEntity.getCreatedAt())
            .hasFieldOrPropertyWithValue("lastUpdateAt", releaseJpaEntity.getLastUpdateAt());


    }

    @Test
    void givenId_releaseNotFound() {
        var uuid = UUID.randomUUID();
        when(releaseJpaRepository.findByUuid(uuid))
            .thenReturn(Optional.empty());
        assertThatThrownBy(() -> jpaReleaseService.get(uuid))
            .isInstanceOf(NonExistentReleaseException.class)
            .extracting("message")
            .isEqualTo(String.format("Release with uuid: %s is not found", uuid));

    }

    @Test
    void given_returnList() {
        var uuid = UUID.randomUUID();
        var name = "Test release";
        var description = "This is dummy test release";
        var releaseDate = LocalDate.now();
        var createdAt = LocalDateTime.now();
        var lastUpdateAt = LocalDateTime.now();

        when(releaseJpaRepository.findAll())
            .thenReturn(
                List.of(
                    createReleaseJpaEntityRandomId(
                        uuid,
                        name,
                        description,
                        ReleaseStatusJpaEntity.CREATED,
                        releaseDate,
                        createdAt,
                        lastUpdateAt)));
        var releases = jpaReleaseService.list(null, null, null, null, null, null);
        assertThat(releases)
            .hasSize(1)
            .anySatisfy(release -> {
                assertThat(release)
                    .hasFieldOrPropertyWithValue("id", uuid)
                    .hasFieldOrPropertyWithValue("name", name)
                    .hasFieldOrPropertyWithValue("description", description)
                    .hasFieldOrPropertyWithValue("releaseDate", releaseDate)
                    .hasFieldOrPropertyWithValue("createdAt", createdAt)
                    .hasFieldOrPropertyWithValue("lastUpdateAt", lastUpdateAt);
            });
    }

    @Test
    void givenEmptyList_returnEmptyList() {
        when(releaseJpaRepository.findAll()).thenReturn(List.of());
        var releases = jpaReleaseService.list(null, null, null, null, null, null);
        assertThat(releases).isEmpty();
    }

    @Test
    void givenId_deleteRelease_throwsException() {
        var uuid = UUID.randomUUID();
        when(releaseJpaRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> jpaReleaseService.delete(uuid))
            .isInstanceOf(NonExistentReleaseException.class)
            .extracting("message")
            .isEqualTo(String.format("Release with uuid: %s is not found", uuid));

    }

    @Test
    void givenId_deleteRelease_deleteThrowsException() {
        var uuid = UUID.randomUUID();
        var releaseJpaEntity = createReleaseJpaEntityRandomId(
            uuid,
            "Test release",
            "This is dummy test release",
            ReleaseStatusJpaEntity.CREATED,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());
        when(releaseJpaRepository.findByUuid(uuid)).thenReturn(Optional.of(releaseJpaEntity));
        doThrow(RuntimeException.class).when(releaseJpaRepository).deleteById(releaseJpaEntity.getId());
        assertThatThrownBy(() -> jpaReleaseService.delete(uuid))
            .isInstanceOf(ReleaseTrackerException.class);
    }

}
