package com.vladimirkolarevic.releasetracker.rest;

import com.vladimirkolarevic.releasetracker.rest.exception.RestApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v1/releases")
public class ReleaseRestController {

    private final ReleaseRestService releaseRestService;

    public ReleaseRestController(ReleaseRestService releaseRestService) {
        this.releaseRestService = releaseRestService;

    }

    @Operation(summary = "Get release by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Release data", content = @Content(schema = @Schema(implementation = ReleaseResponse.class))),
        @ApiResponse(responseCode = "404", description = "Error data", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReleaseResponse> getById(@PathVariable(value = "id", required = false) String id) {
        var releaseResponse = releaseRestService.get(id);
        return ResponseEntity.status(releaseResponse.httpStatus()).body(releaseResponse);
    }

    @Operation(summary = "Filter releases")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = " List of releases", content = @Content(schema = @Schema(implementation = ReleaseResponse.class, type = "List"))),
        @ApiResponse(responseCode = "400", description = "Error data", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    @GetMapping
    public ResponseEntity<List<ReleaseResponse>> get(@RequestParam(value = "name", required = false)
                                                     String name,
                                                     @RequestParam(value = "description", required = false)
                                                     String description,
                                                     @RequestParam(value = "status", required = false)
                                                     ReleaseResponseStatus status,
                                                     @RequestParam(value = "releaseDate", required = false)
                                                     String releaseDate,
                                                     @RequestParam(value = "createdAt", required = false)
                                                     String createdAt,
                                                     @RequestParam(value = "lastUpdateAt", required = false)
                                                     String lastUpdateAt) {
        return ResponseEntity.ok(
            releaseRestService.get(name, description, status, releaseDate, createdAt, lastUpdateAt));
    }

    @Operation(summary = "Save releases")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created release", content = @Content(schema = @Schema(implementation = ReleaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "Error data", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    @PostMapping
    public ResponseEntity<ReleaseResponse> saveRelease(@RequestBody ReleaseRequest releaseRequest) {
        var releaseResponse = releaseRestService.save(releaseRequest);
        return ResponseEntity.status(releaseResponse.httpStatus()).body(releaseResponse);
    }

    @Operation(summary = "Save releases")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated release", content = @Content(schema = @Schema(implementation = ReleaseResponse.class))),
        @ApiResponse(responseCode = "201", description = "Created release", content = @Content(schema = @Schema(implementation = ReleaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "Error data", content = @Content(schema = @Schema(implementation = RestApiError.class))),
        @ApiResponse(responseCode = "404", description = "Error data", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReleaseResponse> updateRelease(@PathVariable("id") String id,
                                                         @RequestBody ReleaseRequest releaseRequest) {
        var updatedRelease = releaseRestService.update(id, releaseRequest);
        return ResponseEntity.status(updatedRelease.httpStatus()).body(updatedRelease);
    }

    @Operation(summary = "Delete releases")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Delete release", content = @Content(schema = @Schema(implementation = ReleaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "Error data", content = @Content(schema = @Schema(implementation = RestApiError.class))),
        @ApiResponse(responseCode = "404", description = "Error data", content = @Content(schema = @Schema(implementation = RestApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        releaseRestService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
