package com.vladimirkolarevic.releasetracker;

import com.vladimirkolarevic.releasetracker.container.PostgreSQLContainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
class ReleaseTrackerAppApplicationTests {

    @Test
    void contextLoads() {
    }

}
