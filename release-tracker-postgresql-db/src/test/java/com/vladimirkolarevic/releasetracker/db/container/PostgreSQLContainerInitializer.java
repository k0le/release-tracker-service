package com.vladimirkolarevic.releasetracker.db.container;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

public class PostgreSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer();

    private static void startContainer() {
        Startables.deepStart(Stream.of(postgreSQLContainer)).join();
    }

    private static Map<String, Object> createConnectionConfiguration() {
        return Map.of(
                "spring.datasource.url", postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username", postgreSQLContainer.getUsername(),
                "spring.datasource.password", postgreSQLContainer.getPassword()
        );
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        startContainer();
        var configurableEnvironment = applicationContext.getEnvironment();
        var testcontainers = new MapPropertySource("testcontainers",createConnectionConfiguration());
        configurableEnvironment.getPropertySources().addFirst(testcontainers);
    }
}
