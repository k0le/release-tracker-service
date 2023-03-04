package com.vladimirkolarevic.releasetracker.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vladimirkolarevic.releasetracker.domain.Release;
import com.vladimirkolarevic.releasetracker.domain.ReleaseService;
import com.vladimirkolarevic.releasetracker.domain.ReleaseStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ReleaseRestController.class)
@ContextConfiguration(classes = ReleaseRestConfig.class)
class ReleaseTrackerRestApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReleaseService releaseService;


    @Test
    void returnAllElements() throws Exception {
        var uuid = UUID.randomUUID();
        var name = "Create release";
        var description = "Release Created";
        var releaseDate = LocalDate.now();
        var createdAt = LocalDateTime.now();
        var lastUpdateAt = LocalDateTime.now();
        when(releaseService.list(null, null, null, null, null, null)).thenReturn(List.of(new Release(uuid,
            name,
            description,
            ReleaseStatus.CREATED,
            releaseDate,
            createdAt,
            lastUpdateAt)));

        mockMvc.perform(get("/v1/releases"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(uuid.toString()))
            .andExpect(jsonPath("$[0].name").value(name))
            .andExpect(jsonPath("$[0].description").value(description))
            .andExpect(jsonPath("$[0].status").value(ReleaseResponseStatus.CREATED.name()))
            .andExpect(jsonPath("$[0].releaseDate").value(releaseDate.format(DateTimeFormatter.ISO_DATE)))
            .andExpect(jsonPath("$[0].createdAt").value(createdAt.format(DateTimeFormatter.ISO_DATE_TIME)))
            .andExpect(jsonPath("$[0].lastUpdateAt").value(lastUpdateAt.format(DateTimeFormatter.ISO_DATE_TIME)));
    }

}
