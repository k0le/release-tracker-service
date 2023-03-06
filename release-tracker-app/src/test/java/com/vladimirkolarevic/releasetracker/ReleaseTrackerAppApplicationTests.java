package com.vladimirkolarevic.releasetracker;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimirkolarevic.releasetracker.container.PostgreSQLContainerInitializer;
import com.vladimirkolarevic.releasetracker.rest.ReleaseRequest;
import com.vladimirkolarevic.releasetracker.rest.ReleaseResponse;
import com.vladimirkolarevic.releasetracker.rest.ReleaseResponseStatus;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
class ReleaseTrackerAppApplicationTests {

    private static final String BASE_URL = "http://localhost:";
    @Autowired
    EntityManager entityManager;
    @Autowired
    TransactionTemplate transactionTemplate;
    @Value(value = "${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    @Transactional
    public void tearDown() {
        transactionTemplate.execute(transactionStatus -> {
            entityManager.createQuery("DELETE FROM ReleaseJpaEntity").executeUpdate();
            transactionStatus.flush();
            return null;
        });

    }

    @Test
    void givenRelease_CREATED_POST() throws IOException {
        var releaseDate = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
        var createdAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var lastUpdateAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var releaseRequest =
            new ReleaseRequest("Create release", "Test create release", ReleaseResponseStatus.CREATED, releaseDate);
        var releaseRequestBody = objectMapper.writeValueAsString(releaseRequest);
        var requestEntity = RequestEntity.post(BASE_URL.concat(String.valueOf(port)).concat("/v1/releases"))
            .contentType(MediaType.APPLICATION_JSON).body(releaseRequestBody);
        var responseEntity = restTemplate.exchange(requestEntity, ReleaseResponse.class);
        assertThat(responseEntity)
            .isNotNull()
            .hasFieldOrPropertyWithValue("status", HttpStatus.CREATED)
            .extracting("body")
            .hasNoNullFieldsOrPropertiesExcept("httpStatus")
            .hasFieldOrPropertyWithValue("name", "Create release")
            .hasFieldOrPropertyWithValue("description", "Test create release")
            .hasFieldOrPropertyWithValue("status", ReleaseResponseStatus.CREATED)
            .hasFieldOrPropertyWithValue("releaseDate", releaseDate)
            .hasFieldOrProperty("createdAt").isNotNull()
            .hasFieldOrProperty("lastUpdateAt").isNotNull()
            .extracting("id").isNotNull();

    }

    @Test
    void givenRelease_CREATED_PUT() throws IOException {
        var id = UUID.randomUUID();
        var releaseDate = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
        var createdAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var lastUpdateAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var releaseRequest =
            new ReleaseRequest("Create release", "Test create release", ReleaseResponseStatus.CREATED, releaseDate);
        var releaseRequestBody = objectMapper.writeValueAsString(releaseRequest);
        var url = BASE_URL.concat(String.valueOf(port)).concat("/v1/releases").concat("/").concat(id.toString());
        var requestEntity = RequestEntity.put(url)
            .contentType(MediaType.APPLICATION_JSON).body(releaseRequestBody);
        var responseEntity = restTemplate.exchange(requestEntity, ReleaseResponse.class);
        assertThat(responseEntity)
            .isNotNull()
            .hasFieldOrPropertyWithValue("status", HttpStatus.CREATED)
            .extracting("body")
            .hasNoNullFieldsOrPropertiesExcept("httpStatus")
            .hasFieldOrPropertyWithValue("name", "Create release")
            .hasFieldOrPropertyWithValue("description", "Test create release")
            .hasFieldOrPropertyWithValue("status", ReleaseResponseStatus.CREATED)
            .hasFieldOrPropertyWithValue("releaseDate", releaseDate)
            .hasFieldOrProperty("createdAt").isNotNull()
            .hasFieldOrProperty("lastUpdateAt").isNotNull()
            .hasFieldOrPropertyWithValue("id", id);

    }

    @Test
    void givenRelease_OK_PUT() throws IOException {
        var id = UUID.randomUUID();
        var releaseDate = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
        var createdAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var lastUpdateAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var releaseRequest =
            new ReleaseRequest("Create release", "Test create release", ReleaseResponseStatus.CREATED, releaseDate);
        var releaseRequestBody = objectMapper.writeValueAsString(releaseRequest);
        var url = BASE_URL.concat(String.valueOf(port)).concat("/v1/releases").concat("/").concat(id.toString());
        var requestEntity = RequestEntity.put(url)
            .contentType(MediaType.APPLICATION_JSON).body(releaseRequestBody);
        var responseEntity = restTemplate.exchange(requestEntity, ReleaseResponse.class);
        assertThat(responseEntity)
            .isNotNull()
            .hasFieldOrPropertyWithValue("status", HttpStatus.CREATED)
            .extracting("body")
            .hasNoNullFieldsOrPropertiesExcept("httpStatus")
            .hasFieldOrPropertyWithValue("name", releaseRequest.name())
            .hasFieldOrPropertyWithValue("description", releaseRequest.description())
            .hasFieldOrPropertyWithValue("status", ReleaseResponseStatus.CREATED)
            .hasFieldOrPropertyWithValue("releaseDate", releaseRequest.releaseDate())
            .hasFieldOrProperty("createdAt").isNotNull()
            .hasFieldOrProperty("lastUpdateAt").isNotNull()
            .hasFieldOrPropertyWithValue("id", id);

        var releaseDateUpdate = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
        var createdAtUpdate = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var lastUpdateAtUpdate = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var releaseRequestUpdate =
            new ReleaseRequest("Create release Update", "Test create release Update", ReleaseResponseStatus.DONE,
                releaseDateUpdate);
        var releaseRequestBodyUpdate = objectMapper.writeValueAsString(releaseRequestUpdate);
        var requestEntityUpdate = RequestEntity.put(url)
            .contentType(MediaType.APPLICATION_JSON).body(releaseRequestBodyUpdate);
        var responseEntityUpdate = restTemplate.exchange(requestEntityUpdate, ReleaseResponse.class);
        assertThat(responseEntityUpdate)
            .isNotNull()
            .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
            .extracting("body")
            .hasNoNullFieldsOrPropertiesExcept("httpStatus")
            .hasFieldOrPropertyWithValue("name", releaseRequestUpdate.name())
            .hasFieldOrPropertyWithValue("description", releaseRequestUpdate.description())
            .hasFieldOrPropertyWithValue("status", ReleaseResponseStatus.DONE)
            .hasFieldOrPropertyWithValue("releaseDate", releaseRequestUpdate.releaseDate())
            .hasFieldOrProperty("createdAt").isNotNull()
            .hasFieldOrProperty("lastUpdateAt").isNotNull()
            .hasFieldOrPropertyWithValue("id", id);

    }

    @Test
    void givenRelease_saved_deleted() throws JsonProcessingException {
        var releaseDate = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
        var createdAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var lastUpdateAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var releaseRequest =
            new ReleaseRequest("Create release", "Test create release", ReleaseResponseStatus.CREATED, releaseDate);
        var releaseRequestBody = objectMapper.writeValueAsString(releaseRequest);
        var requestEntity = RequestEntity.post(BASE_URL.concat(String.valueOf(port)).concat("/v1/releases"))
            .contentType(MediaType.APPLICATION_JSON).body(releaseRequestBody);
        var responseEntity = restTemplate.exchange(requestEntity, ReleaseResponse.class);
        assertThat(responseEntity)
            .isNotNull()
            .hasFieldOrPropertyWithValue("status", HttpStatus.CREATED)
            .extracting("body")
            .hasNoNullFieldsOrPropertiesExcept("httpStatus")
            .hasFieldOrPropertyWithValue("name", "Create release")
            .hasFieldOrPropertyWithValue("description", "Test create release")
            .hasFieldOrPropertyWithValue("status", ReleaseResponseStatus.CREATED)
            .hasFieldOrPropertyWithValue("releaseDate", releaseDate)
            .hasFieldOrProperty("createdAt").isNotNull()
            .hasFieldOrProperty("lastUpdateAt").isNotNull()
            .extracting("id").isNotNull();
        var id = responseEntity.getBody().id();
        var requestEntityDelete = RequestEntity.delete(
            BASE_URL.concat(String.valueOf(port)).concat("/v1/releases").concat("/").concat(id.toString())).build();
        var responseEntityDelete = restTemplate.exchange(requestEntityDelete, Void.class);
        assertThat(responseEntityDelete).isNotNull().hasFieldOrPropertyWithValue("status", HttpStatus.NO_CONTENT);
    }

    @Test
    void delete_nonExisting_release_404() {
        var id = UUID.randomUUID();
        var requestEntityDelete = RequestEntity.delete(
            BASE_URL.concat(String.valueOf(port)).concat("/v1/releases").concat("/").concat(id.toString())).build();
        var responseEntityDelete = restTemplate.exchange(requestEntityDelete, Void.class);
        assertThat(responseEntityDelete).isNotNull().hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }

    @Test
    void givenRelease_saved_getById() throws JsonProcessingException {
        var releaseDate = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
        var createdAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var lastUpdateAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var releaseRequest =
            new ReleaseRequest("Create release", "Test create release", ReleaseResponseStatus.CREATED, releaseDate);
        var releaseRequestBody = objectMapper.writeValueAsString(releaseRequest);
        var requestEntity = RequestEntity.post(BASE_URL.concat(String.valueOf(port)).concat("/v1/releases"))
            .contentType(MediaType.APPLICATION_JSON).body(releaseRequestBody);
        var responseEntity = restTemplate.exchange(requestEntity, ReleaseResponse.class);
        assertThat(responseEntity)
            .isNotNull()
            .hasFieldOrPropertyWithValue("status", HttpStatus.CREATED)
            .extracting("body")
            .hasNoNullFieldsOrPropertiesExcept("httpStatus")
            .hasFieldOrPropertyWithValue("name", "Create release")
            .hasFieldOrPropertyWithValue("description", "Test create release")
            .hasFieldOrPropertyWithValue("status", ReleaseResponseStatus.CREATED)
            .hasFieldOrPropertyWithValue("releaseDate", releaseDate)
            .hasFieldOrProperty("createdAt").isNotNull()
            .hasFieldOrProperty("lastUpdateAt").isNotNull()
            .extracting("id").isNotNull();
        var id = responseEntity.getBody().id();
        var requestEntityGet = RequestEntity.get(
            BASE_URL.concat(String.valueOf(port)).concat("/v1/releases").concat("/").concat(id.toString())).build();
        var responseEntityGet = restTemplate.exchange(requestEntityGet, ReleaseResponse.class);
        assertThat(responseEntityGet)
            .isNotNull()
            .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
            .extracting("body")
            .hasNoNullFieldsOrPropertiesExcept("httpStatus")
            .hasFieldOrPropertyWithValue("name", "Create release")
            .hasFieldOrPropertyWithValue("description", "Test create release")
            .hasFieldOrPropertyWithValue("status", ReleaseResponseStatus.CREATED)
            .hasFieldOrPropertyWithValue("releaseDate", releaseDate)
            .hasFieldOrProperty("createdAt").isNotNull()
            .hasFieldOrProperty("lastUpdateAt").isNotNull()
            .extracting("id").isNotNull().isEqualTo(id);
    }

    @Test
    void getById_nonExistentId_404() {
        var id = UUID.randomUUID();
        var requestEntityGet = RequestEntity.get(
            BASE_URL.concat(String.valueOf(port)).concat("/v1/releases").concat("/").concat(id.toString())).build();
        var responseEntityGet = restTemplate.exchange(requestEntityGet, ReleaseResponse.class);
        assertThat(responseEntityGet)
            .isNotNull()
            .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);

    }

    @Test
    void givenRelease_saved_filter() throws JsonProcessingException {
        var releaseDate = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
        var createdAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var lastUpdateAt = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        var releaseRequest =
            new ReleaseRequest("Create release", "Test create release", ReleaseResponseStatus.CREATED, releaseDate);
        var releaseRequestBody = objectMapper.writeValueAsString(releaseRequest);
        var requestEntity = RequestEntity.post(BASE_URL.concat(String.valueOf(port)).concat("/v1/releases"))
            .contentType(MediaType.APPLICATION_JSON).body(releaseRequestBody);
        var responseEntity = restTemplate.exchange(requestEntity, ReleaseResponse.class);
        assertThat(responseEntity)
            .isNotNull()
            .hasFieldOrPropertyWithValue("status", HttpStatus.CREATED)
            .extracting("body")
            .hasNoNullFieldsOrPropertiesExcept("httpStatus")
            .hasFieldOrPropertyWithValue("name", releaseRequest.name())
            .hasFieldOrPropertyWithValue("description", releaseRequest.description())
            .hasFieldOrPropertyWithValue("status", releaseRequest.status())
            .hasFieldOrPropertyWithValue("releaseDate", releaseRequest.releaseDate())
            .hasFieldOrProperty("createdAt").isNotNull()
            .hasFieldOrProperty("lastUpdateAt").isNotNull()
            .extracting("id").isNotNull();

        var requestEntityGet = RequestEntity.get(
            BASE_URL.concat(String.valueOf(port)).concat("/v1/releases").concat("?").concat("description=")
                .concat(releaseRequest.description())).build();
        var responseEntityGet =
            restTemplate.exchange(requestEntityGet, new ParameterizedTypeReference<List<ReleaseResponse>>() {
            });
        assertThat(responseEntityGet)
            .isNotNull()
            .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
            .extracting(ResponseEntity::getBody)
            .satisfies(releaseResponses ->
                assertThat(releaseResponses)
                    .hasSize(1)
                    .element(0)
                    .hasNoNullFieldsOrPropertiesExcept("httpStatus")
                    .hasFieldOrPropertyWithValue("name", releaseRequest.name())
                    .hasFieldOrPropertyWithValue("description", releaseRequest.description())
                    .hasFieldOrPropertyWithValue("status", releaseRequest.status())
                    .hasFieldOrPropertyWithValue("releaseDate", releaseRequest.releaseDate())
                    .hasFieldOrProperty("createdAt").isNotNull()
                    .hasFieldOrProperty("lastUpdateAt").isNotNull()
                    .extracting("id").isNotNull()

            );


    }
}
