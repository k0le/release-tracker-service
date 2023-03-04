package com.vladimirkolarevic.releasetracker.db;

import static com.vladimirkolarevic.releasetracker.db.ReleaseHelper.createRelease;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vladimirkolarevic.releasetracker.db.container.PostgreSQLContainerInitializer;
import com.vladimirkolarevic.releasetracker.domain.Release;
import com.vladimirkolarevic.releasetracker.domain.ReleaseService;
import com.vladimirkolarevic.releasetracker.domain.ReleaseStatus;
import com.vladimirkolarevic.releasetracker.domain.exception.NonExistentReleaseException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(classes = ReleaseTrackerConfig.class)
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
@EnableAutoConfiguration
class ReleaseTrackerPostgresDbTest {
    @Autowired
    EntityManager entityManager;
    @Autowired
    TransactionTemplate transactionTemplate;
    @Autowired
    private ReleaseService releaseService;

    private static Stream<Arguments> streamReleases() {
        return Stream.of(
            Arguments.of(
                createRelease(
                    UUID.randomUUID(),
                    "Create release",
                    "Release Created",
                    ReleaseStatus.CREATED,
                    LocalDate.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now())

            ),
            Arguments.of(
                createRelease(
                    UUID.randomUUID(),
                    "Release done",
                    "Release executed",
                    ReleaseStatus.DONE,
                    LocalDate.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now())

            ),
            Arguments.of(
                createRelease(
                    UUID.randomUUID(),
                    "Release on DEV",
                    "Release on DEV environment",
                    ReleaseStatus.ON_DEV,
                    LocalDate.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now())

            ),
            Arguments.of(
                createRelease(
                    UUID.randomUUID(),
                    "Release in development",
                    "Release is currently in development",
                    ReleaseStatus.IN_DEVELOPMENT,
                    LocalDate.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now())

            ),
            Arguments.of(
                createRelease(
                    UUID.randomUUID(),
                    "Release on production",
                    "Release is currently in production environment",
                    ReleaseStatus.ON_PROD,
                    LocalDate.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now())

            ),
            Arguments.of(
                createRelease(
                    UUID.randomUUID(),
                    "Release on staging",
                    "Release is currently in staging environment",
                    ReleaseStatus.ON_STAGING,
                    LocalDate.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now())

            ),
            Arguments.of(
                createRelease(
                    UUID.randomUUID(),
                    "Release QA done on dev",
                    "Release QA is don on dev environment",
                    ReleaseStatus.QA_DONE_ON_DEV,
                    LocalDate.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now())
            ),
            Arguments.of(
                createRelease(
                    UUID.randomUUID(),
                    "Release QA done on staging",
                    "Release QA is don on staging environment",
                    ReleaseStatus.QA_DONE_ON_STAGING,
                    LocalDate.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now())
            )


        );
    }

    private static Stream<Arguments> streamReleasesWithUuid() {
        var releaseCreated = createRelease(
            UUID.randomUUID(),
            "Create release",
            "Release Created",
            ReleaseStatus.CREATED,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());
        var releaseDone = createRelease(
            UUID.randomUUID(),
            "Release done",
            "Release executed",
            ReleaseStatus.DONE,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());

        var releaseOnDev = createRelease(
            UUID.randomUUID(),
            "Release on DEV",
            "Release on DEV environment",
            ReleaseStatus.ON_DEV,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());
        var releaseInDevelopment = createRelease(
            UUID.randomUUID(),
            "Release in development",
            "Release is currently in development",
            ReleaseStatus.IN_DEVELOPMENT,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());

        var releaseOnProd = createRelease(
            UUID.randomUUID(),
            "Release in development",
            "Release is currently in development",
            ReleaseStatus.IN_DEVELOPMENT,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());

        var releaseOnStaging = createRelease(
            UUID.randomUUID(),
            "Release on staging",
            "Release is currently in staging environment",
            ReleaseStatus.ON_STAGING,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());

        var releaseQaDoneOnDev = createRelease(
            UUID.randomUUID(),
            "Release QA done on staging",
            "Release QA is don on staging environment",
            ReleaseStatus.QA_DONE_ON_DEV,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());

        var releaseQaDoneOnStaging = createRelease(
            UUID.randomUUID(),
            "Release QA done on staging",
            "Release QA is don on staging environment",
            ReleaseStatus.QA_DONE_ON_STAGING,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());


        return Stream.of(
            Arguments.of(
                releaseCreated, releaseCreated.id()

            ),
            Arguments.of(
                releaseDone, releaseDone.id()
            ),
            Arguments.of(

                releaseOnDev, releaseOnDev.id()
            ),
            Arguments.of(
                releaseInDevelopment, releaseInDevelopment.id()

            ),
            Arguments.of(
                releaseOnProd, releaseOnProd.id()

            ),
            Arguments.of(
                releaseOnStaging, releaseOnStaging.id()

            ),
            Arguments.of(
                releaseQaDoneOnDev, releaseQaDoneOnDev.id()
            ),
            Arguments.of(
                releaseQaDoneOnStaging, releaseQaDoneOnStaging.id()
            )


        );
    }

    @AfterEach
    @Transactional
    public void tearDown() {
        transactionTemplate.execute(transactionStatus -> {
            entityManager.createQuery("DELETE FROM ReleaseJpaEntity").executeUpdate();
            transactionStatus.flush();
            return null;
        });

    }

    @ParameterizedTest
    @MethodSource("streamReleases")
    void givenReleases_saved_returnedSaved(Release sourceRelease) {
        var savedRelease = releaseService.save(sourceRelease);
        assertThat(savedRelease).isNotNull()
            .hasFieldOrPropertyWithValue("id", sourceRelease.id())
            .hasFieldOrPropertyWithValue("name", sourceRelease.name())
            .hasFieldOrPropertyWithValue("description", sourceRelease.description())
            .hasFieldOrPropertyWithValue("status", sourceRelease.status())
            .hasFieldOrPropertyWithValue("releaseDate", sourceRelease.releaseDate())
            .hasFieldOrPropertyWithValue("createdAt", sourceRelease.createdAt())
            .hasFieldOrPropertyWithValue("lastUpdateAt", sourceRelease.lastUpdateAt());
    }

    @ParameterizedTest
    @MethodSource("streamReleasesWithUuid")
    void givenRelease_saved_findByUuid(Release sourceRelease, UUID uuid) {
        releaseService.save(sourceRelease);
        var foundRelease = releaseService.get(sourceRelease.id());
        assertThat(foundRelease).isNotNull()
            .hasFieldOrPropertyWithValue("id", sourceRelease.id())
            .hasFieldOrPropertyWithValue("name", sourceRelease.name())
            .hasFieldOrPropertyWithValue("description", sourceRelease.description())
            .hasFieldOrPropertyWithValue("status", sourceRelease.status())
            .hasFieldOrPropertyWithValue("releaseDate", sourceRelease.releaseDate())
            .hasFieldOrPropertyWithValue("createdAt", sourceRelease.createdAt())
            .hasFieldOrPropertyWithValue("lastUpdateAt", sourceRelease.lastUpdateAt());

    }

    @Test
    void givenRelease_saved_notFoundByUuid() {

        releaseService.save(createRelease(
            UUID.randomUUID(),
            "Create release",
            "Release Created",
            ReleaseStatus.CREATED,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now()));

        var uuid = UUID.randomUUID();
        assertThatThrownBy(() -> releaseService.get(uuid)).isInstanceOf(NonExistentReleaseException.class)
            .extracting("message").isEqualTo(String.format("Release with uuid: %s is not found", uuid));

    }

    @Test
    void givenRelease_saved_listAllSavedReleases() {
        var release = createRelease(
            UUID.randomUUID(),
            "Create release",
            "Release Created",
            ReleaseStatus.CREATED,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());
        releaseService.save(release);
        var savedReleases = releaseService.list();

        assertThat(savedReleases).isNotEmpty().hasSize(1).element(0)
            .hasFieldOrPropertyWithValue("id", release.id())
            .hasFieldOrPropertyWithValue("name", release.name())
            .hasFieldOrPropertyWithValue("description", release.description())
            .hasFieldOrPropertyWithValue("status", release.status())
            .hasFieldOrPropertyWithValue("releaseDate", release.releaseDate())
            .hasFieldOrPropertyWithValue("createdAt", release.createdAt())
            .hasFieldOrPropertyWithValue("lastUpdateAt", release.lastUpdateAt());
    }

    @Test
    void givenRelease_saved_deleted() {
        var release = createRelease(
            UUID.randomUUID(),
            "Create release",
            "Release Created",
            ReleaseStatus.CREATED,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());
        releaseService.save(release);
        releaseService.delete(release.id());
        assertThatThrownBy(() -> releaseService.get(release.id())).isInstanceOf(NonExistentReleaseException.class)
            .extracting("message").isEqualTo(String.format("Release with uuid: %s is not found", release.id()));

    }

    @Test
    void givenUuid_notSaved_delete_throwException() {
        var uuid = UUID.randomUUID();
        assertThatThrownBy(() -> releaseService.delete(uuid)).isInstanceOf(NonExistentReleaseException.class)
            .extracting("message").isEqualTo(String.format("Release with uuid: %s is not found", uuid));

    }

    @Test
    void givenRelease_saved_count() {
        var release = createRelease(
            UUID.randomUUID(),
            "Create release",
            "Release Created",
            ReleaseStatus.CREATED,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());
        releaseService.save(release);
        var total = releaseService.count();
        assertThat(total).isEqualTo(1);
    }

    @Test
    void givenRelease_saved_updated() {
        var uuid = UUID.randomUUID();
        var release = createRelease(
            uuid,
            "Create release",
            "Release Created",
            ReleaseStatus.CREATED,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());

        releaseService.save(release);
        var savedRelease = releaseService.get(uuid);

        assertThat(savedRelease).isNotNull()
            .hasFieldOrPropertyWithValue("id", release.id())
            .hasFieldOrPropertyWithValue("name", release.name())
            .hasFieldOrPropertyWithValue("description", release.description())
            .hasFieldOrPropertyWithValue("status", release.status())
            .hasFieldOrPropertyWithValue("releaseDate", release.releaseDate())
            .hasFieldOrPropertyWithValue("createdAt", release.createdAt())
            .hasFieldOrPropertyWithValue("lastUpdateAt", release.lastUpdateAt());
        var releaseChanges = createRelease(
            uuid,
            "Release Done",
            "Release Done",
            ReleaseStatus.DONE,
            LocalDate.now(),
            LocalDateTime.now(),
            LocalDateTime.now());
        releaseService.update(releaseChanges);

        var foundRelease = releaseService.get(uuid);

        assertThat(foundRelease).isNotNull()
            .hasFieldOrPropertyWithValue("id", releaseChanges.id())
            .hasFieldOrPropertyWithValue("name", releaseChanges.name())
            .hasFieldOrPropertyWithValue("description", releaseChanges.description())
            .hasFieldOrPropertyWithValue("status", releaseChanges.status())
            .hasFieldOrPropertyWithValue("releaseDate", releaseChanges.releaseDate())
            .hasFieldOrPropertyWithValue("createdAt", releaseChanges.createdAt())
            .hasFieldOrPropertyWithValue("lastUpdateAt", releaseChanges.lastUpdateAt());
    }
}
