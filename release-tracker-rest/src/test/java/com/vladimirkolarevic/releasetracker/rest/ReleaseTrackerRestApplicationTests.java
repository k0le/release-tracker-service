package com.vladimirkolarevic.releasetracker.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimirkolarevic.releasetracker.domain.Release;
import com.vladimirkolarevic.releasetracker.domain.ReleaseService;
import com.vladimirkolarevic.releasetracker.domain.ReleaseStatus;
import com.vladimirkolarevic.releasetracker.domain.exception.NonExistentReleaseException;
import com.vladimirkolarevic.releasetracker.domain.exception.ReleaseTrackerException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

@WebMvcTest(controllers = ReleaseRestController.class)
@ContextConfiguration(classes = ReleaseRestConfig.class)
class ReleaseTrackerRestApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReleaseService releaseService;

    private static final String RELEASES_URL = "/v1/releases";


    @ParameterizedTest
    @MethodSource("releasesStream")
    void givenListReleases_get_returnsReleases(String name, String description, ReleaseStatus releaseStatus,
                                               String releaseDate,
                                               String createdAt, String lastUpdateAt, List<Release> releasesMock,
                                               List<ReleaseResponse> releasesToBeReturned)
        throws Exception {
        var releaseDateMock =
            releaseDate != null ? LocalDate.from(DateTimeFormatter.ISO_DATE.parse(releaseDate)) : null;
        var createdAtMock =
            createdAt != null ? LocalDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(createdAt)) : null;
        var lastUpdateAtMock =
            lastUpdateAt != null ? LocalDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(lastUpdateAt)) : null;
        when(releaseService.list(name, description, releaseStatus, releaseDateMock, createdAtMock,
            lastUpdateAtMock)).thenReturn(releasesMock);
        var queryParams = new LinkedMultiValueMap<String, String>();
        if (name != null) {
            queryParams.add("name", name);
        }
        if (description != null) {
            queryParams.add("description", description);
        }
        if (releaseStatus != null) {
            queryParams.add("status", releaseStatus.name());
        }
        if (releaseDate != null) {
            queryParams.add("releaseDate", releaseDate);
        }
        if (createdAt != null) {
            queryParams.add("createdAt", createdAt);
        }
        if (lastUpdateAt != null) {
            queryParams.add("lastUpdateAt", lastUpdateAt);
        }
        mockMvc.perform(get(RELEASES_URL).queryParams(queryParams))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(releasesToBeReturned.size())))
            .andExpect(jsonPath("$[0].id").value(releasesToBeReturned.get(0).id().toString()))
            .andExpect(jsonPath("$[0].name").value(releasesToBeReturned.get(0).name()))
            .andExpect(jsonPath("$[0].description").value(releasesToBeReturned.get(0).description()))
            .andExpect(jsonPath("$[0].status").value(
                ReleaseResponseStatus.valueOf(releasesToBeReturned.get(0).status().name()).name()))
            .andExpect(jsonPath("$[0].releaseDate").value(
                releasesToBeReturned.get(0).releaseDate()))
            .andExpect(jsonPath("$[0].createdAt").value(
                releasesToBeReturned.get(0).createdAt()))
            .andExpect(jsonPath("$[0].lastUpdateAt").value(
                releasesToBeReturned.get(0).lastUpdateAt()));
    }

    @Test
    void givenRelease_saved() throws Exception {
        var release =
            new Release(null, "Created release", "Test release", ReleaseStatus.CREATED, LocalDate.now(),
                LocalDateTime.now(), LocalDateTime.now());

        var returnedRelease = new Release(UUID.randomUUID(), release.name(), release.description(), release.status(),
            release.releaseDate(), release.createdAt(), release.lastUpdateAt());

        var releaseRequest = new ReleaseRequest(release.name(), release.description(),
            ReleaseResponseStatus.valueOf(release.status().name()),
            DateTimeFormatter.ISO_DATE.format(release.releaseDate()),
            DateTimeFormatter.ISO_DATE_TIME.format(release.createdAt()),
            DateTimeFormatter.ISO_DATE_TIME.format(release.lastUpdateAt()));

        var requestBody = objectMapper.writeValueAsString(releaseRequest);

        when(releaseService.save(release)).thenReturn(returnedRelease);
        mockMvc.perform(post(RELEASES_URL).content(requestBody).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(release.name()))
            .andExpect(jsonPath("$.description").value(release.description()))
            .andExpect(jsonPath("$.status").value(ReleaseResponseStatus.valueOf(release.status().name()).name()))
            .andExpect(jsonPath("$.createdAt").value(release.createdAt().format(DateTimeFormatter.ISO_DATE_TIME)))
            .andExpect(jsonPath("$.lastUpdateAt").value(release.lastUpdateAt().format(DateTimeFormatter.ISO_DATE_TIME)))
            .andExpect(jsonPath("$.releaseDate").value(release.releaseDate().format(DateTimeFormatter.ISO_DATE)))
            .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void givenRelease_newSaved_put() throws Exception {
        var uuid = UUID.randomUUID();
        var release =
            new Release(uuid, "Created release", "Test release", ReleaseStatus.CREATED, LocalDate.now(),
                LocalDateTime.now(), LocalDateTime.now());

        var returnedRelease = new Release(uuid, release.name(), release.description(), release.status(),
            release.releaseDate(), release.createdAt(), release.lastUpdateAt());



        var releaseRequest = new ReleaseRequest(release.name(), release.description(),
            ReleaseResponseStatus.valueOf(release.status().name()),
            DateTimeFormatter.ISO_DATE.format(release.releaseDate()),
            DateTimeFormatter.ISO_DATE_TIME.format(release.createdAt()),
            DateTimeFormatter.ISO_DATE_TIME.format(release.lastUpdateAt()));

        var requestBody = objectMapper.writeValueAsString(releaseRequest);
        when(releaseService.get(uuid)).thenReturn(null);
        when(releaseService.update(release)).thenReturn(returnedRelease);
        mockMvc.perform(put(RELEASES_URL.concat("/").concat(uuid.toString())).content(requestBody).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(release.name()))
            .andExpect(jsonPath("$.description").value(release.description()))
            .andExpect(jsonPath("$.status").value(ReleaseResponseStatus.valueOf(release.status().name()).name()))
            .andExpect(jsonPath("$.createdAt").value(release.createdAt().format(DateTimeFormatter.ISO_DATE_TIME)))
            .andExpect(jsonPath("$.lastUpdateAt").value(release.lastUpdateAt().format(DateTimeFormatter.ISO_DATE_TIME)))
            .andExpect(jsonPath("$.releaseDate").value(release.releaseDate().format(DateTimeFormatter.ISO_DATE)))
            .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void givenRelease_update_put() throws Exception {
        var uuid = UUID.randomUUID();
        var release =
            new Release(uuid, "Created release", "Test release", ReleaseStatus.CREATED, LocalDate.now(),
                LocalDateTime.now(), LocalDateTime.now());

        var returnedRelease = new Release(uuid, release.name(), release.description(), release.status(),
            release.releaseDate(), release.createdAt(), release.lastUpdateAt());



        var releaseRequest = new ReleaseRequest(release.name(), release.description(),
            ReleaseResponseStatus.valueOf(release.status().name()),
            DateTimeFormatter.ISO_DATE.format(release.releaseDate()),
            DateTimeFormatter.ISO_DATE_TIME.format(release.createdAt()),
            DateTimeFormatter.ISO_DATE_TIME.format(release.lastUpdateAt()));

        var requestBody = objectMapper.writeValueAsString(releaseRequest);
        when(releaseService.get(uuid)).thenReturn(release);
        when(releaseService.update(release)).thenReturn(returnedRelease);
        mockMvc.perform(put(RELEASES_URL.concat("/").concat(uuid.toString())).content(requestBody).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(release.name()))
            .andExpect(jsonPath("$.description").value(release.description()))
            .andExpect(jsonPath("$.status").value(ReleaseResponseStatus.valueOf(release.status().name()).name()))
            .andExpect(jsonPath("$.createdAt").value(release.createdAt().format(DateTimeFormatter.ISO_DATE_TIME)))
            .andExpect(jsonPath("$.lastUpdateAt").value(release.lastUpdateAt().format(DateTimeFormatter.ISO_DATE_TIME)))
            .andExpect(jsonPath("$.releaseDate").value(release.releaseDate().format(DateTimeFormatter.ISO_DATE)))
            .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void givenRelease_delete() throws Exception {
        var uuid = UUID.randomUUID();
        doNothing().when(releaseService).delete(uuid);

        mockMvc.perform(delete(RELEASES_URL.concat("/").concat(uuid.toString())))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void givenRelease_delete_throwReleaseTrackerException() throws Exception {
        var uuid = UUID.randomUUID();
        doThrow(new ReleaseTrackerException("Exception while deleting")).when(releaseService).delete(uuid);

        mockMvc.perform(delete(RELEASES_URL.concat("/").concat(uuid.toString())))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Exception while deleting"));
    }

    @Test
    void givenRelease_get() throws Exception {
        var uuid = UUID.randomUUID();
        var release =
            new Release(uuid, "Created release", "Test release", ReleaseStatus.CREATED, LocalDate.now(),
                LocalDateTime.now(), LocalDateTime.now());


        when(releaseService.get(uuid)).thenReturn(release);
        mockMvc.perform(get(RELEASES_URL.concat("/").concat(uuid.toString())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(release.name()))
            .andExpect(jsonPath("$.description").value(release.description()))
            .andExpect(jsonPath("$.status").value(ReleaseResponseStatus.valueOf(release.status().name()).name()))
            .andExpect(jsonPath("$.createdAt").value(release.createdAt().format(DateTimeFormatter.ISO_DATE_TIME)))
            .andExpect(jsonPath("$.lastUpdateAt").value(release.lastUpdateAt().format(DateTimeFormatter.ISO_DATE_TIME)))
            .andExpect(jsonPath("$.releaseDate").value(release.releaseDate().format(DateTimeFormatter.ISO_DATE)))
            .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void givenRelease_get_throwNonExistentReleaseException() throws Exception {
        var uuid = UUID.randomUUID();

        when(releaseService.get(uuid)).thenThrow(new NonExistentReleaseException("Release do not exists"));
        mockMvc.perform(get(RELEASES_URL.concat("/").concat(uuid.toString())))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Release do not exists"));
    }


    private static Stream<Arguments> releasesStream() {
        var release =
            new Release(UUID.randomUUID(), "Created release", "Test release", ReleaseStatus.CREATED, LocalDate.now(),
                LocalDateTime.now(), LocalDateTime.now());
        var release2 =
            new Release(UUID.randomUUID(), "Release Done", "Test release done", ReleaseStatus.DONE, LocalDate.now(),
                LocalDateTime.now(), LocalDateTime.now());
        var releaseResponse = new ReleaseResponse(release.id(), release.name(), release.description(),
            ReleaseResponseStatus.valueOf(release.status().name()),
            release.releaseDate().format(DateTimeFormatter.ISO_DATE),
            release.createdAt().format(DateTimeFormatter.ISO_DATE_TIME),
            release.lastUpdateAt().format(DateTimeFormatter.ISO_DATE_TIME), null);

        var releaseResponse2 = new ReleaseResponse(release2.id(), release2.name(), release2.description(),
            ReleaseResponseStatus.valueOf(release2.status().name()),
            release2.releaseDate().format(DateTimeFormatter.ISO_DATE),
            release2.createdAt().format(DateTimeFormatter.ISO_DATE_TIME),
            release2.lastUpdateAt().format(DateTimeFormatter.ISO_DATE_TIME), null);

        return Stream.of(
            Arguments.of(
                null, null, null, null, null, null, List.of(release, release2),
                List.of(releaseResponse, releaseResponse2)
            ),
            Arguments.of(
                "Release Done", null, null, null, null, null, List.of(release2), List.of(releaseResponse2)
            ),
            Arguments.of(
                null, null, ReleaseStatus.DONE, null, null, null, List.of(release2), List.of(releaseResponse2)
            )
        );

    }

}
