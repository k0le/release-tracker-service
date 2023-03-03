package com.vladimirkolarevic.releasetracker.db;

import com.vladimirkolarevic.releasetracker.db.container.PostgreSQLContainerInitializer;
import com.vladimirkolarevic.releasetracker.domain.Release;
import com.vladimirkolarevic.releasetracker.domain.ReleaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = ReleaseTrackerConfig.class)
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
@EnableAutoConfiguration
public class ReleaseTrackerPostgresDbIT {
    @Autowired
    private ReleaseService releaseService;

    @ParameterizedTest
    void givenReleaseDone_returnSavedDoneRelease(){

    }
}
