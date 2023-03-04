package com.vladimirkolarevic.releasetracker.db;

import com.vladimirkolarevic.releasetracker.db.container.PostgreSQLContainerInitializer;
import com.vladimirkolarevic.releasetracker.domain.Release;
import com.vladimirkolarevic.releasetracker.domain.ReleaseService;
import com.vladimirkolarevic.releasetracker.domain.ReleaseStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootTest(classes = ReleaseTrackerConfig.class)
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
@EnableAutoConfiguration
public class ReleaseTrackerPostgresDbIT {
    @Autowired
    private ReleaseService releaseService;
    @ParameterizedTest
    @MethodSource("streamReleases")
    void givenReleases_saved_returnedSaved(Release sourceRelease){
        var savedRelease = releaseService.save(sourceRelease);
        Assertions.assertThat(savedRelease).isNotNull()
                .hasFieldOrPropertyWithValue("id",sourceRelease.id())
                .hasFieldOrPropertyWithValue("name",sourceRelease.name())
                .hasFieldOrPropertyWithValue("description",sourceRelease.description())
                .hasFieldOrPropertyWithValue("status",sourceRelease.status())
                .hasFieldOrPropertyWithValue("releaseDate",sourceRelease.releaseDate())
                .hasFieldOrPropertyWithValue("createdAt",sourceRelease.createdAt())
                .hasFieldOrPropertyWithValue("lastUpdateAt",sourceRelease.lastUpdateAt());
    }

    private static Stream<Arguments> streamReleases(){
        return Stream.of(
          Arguments.of(
                  ReleaseHelper.createRelease(
                          UUID.randomUUID(),
                          "Create release",
                          "Release Created",
                          ReleaseStatus.CREATED,
                          LocalDate.now(),
                          LocalDateTime.now(),
                          LocalDateTime.now())

          ),
                Arguments.of(
                        ReleaseHelper.createRelease(
                                UUID.randomUUID(),
                                "Release done",
                                "Release executed",
                                ReleaseStatus.DONE,
                                LocalDate.now(),
                                LocalDateTime.now(),
                                LocalDateTime.now())

                ),
                Arguments.of(
                        ReleaseHelper.createRelease(
                                UUID.randomUUID(),
                                "Release on DEV",
                                "Release on DEV environment",
                                ReleaseStatus.ON_DEV,
                                LocalDate.now(),
                                LocalDateTime.now(),
                                LocalDateTime.now())

                ),
                Arguments.of(
                        ReleaseHelper.createRelease(
                                UUID.randomUUID(),
                                "Release in development",
                                "Release is currently in development",
                                ReleaseStatus.IN_DEVELOPMENT,
                                LocalDate.now(),
                                LocalDateTime.now(),
                                LocalDateTime.now())

                ),
                Arguments.of(
                        ReleaseHelper.createRelease(
                                UUID.randomUUID(),
                                "Release on production",
                                "Release is currently in production environment",
                                ReleaseStatus.ON_PROD,
                                LocalDate.now(),
                                LocalDateTime.now(),
                                LocalDateTime.now())

                ),
                Arguments.of(
                        ReleaseHelper.createRelease(
                                UUID.randomUUID(),
                                "Release on staging",
                                "Release is currently in staging environment",
                                ReleaseStatus.ON_STAGING,
                                LocalDate.now(),
                                LocalDateTime.now(),
                                LocalDateTime.now())

                ),
                Arguments.of(
                        ReleaseHelper.createRelease(
                                UUID.randomUUID(),
                                "Release on staging",
                                "Release is currently in staging environment",
                                ReleaseStatus.ON_STAGING,
                                LocalDate.now(),
                                LocalDateTime.now(),
                                LocalDateTime.now())

                ),
                Arguments.of(
                        ReleaseHelper.createRelease(
                                UUID.randomUUID(),
                                "Release QA done on dev",
                                "Release QA is don on dev environment",
                                ReleaseStatus.QA_DONE_ON_DEV,
                                LocalDate.now(),
                                LocalDateTime.now(),
                                LocalDateTime.now())
                ),
                Arguments.of(
                        ReleaseHelper.createRelease(
                                UUID.randomUUID(),
                                "Release QA done on dev",
                                "Release QA is don on dev environment",
                                ReleaseStatus.QA_DONE_ON_DEV,
                                LocalDate.now(),
                                LocalDateTime.now(),
                                LocalDateTime.now())
                ),
                Arguments.of(
                        ReleaseHelper.createRelease(
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
}
