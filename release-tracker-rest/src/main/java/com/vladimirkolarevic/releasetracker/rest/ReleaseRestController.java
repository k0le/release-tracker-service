package com.vladimirkolarevic.releasetracker.rest;

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

    @GetMapping("/{id}")
    public ResponseEntity<ReleaseResponse> getById(@PathVariable(value = "id", required = false) String id) {
        var releaseResponse = releaseRestService.get(id);
        return ResponseEntity.status(releaseResponse.httpStatus()).body(releaseResponse);
    }

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
        return ResponseEntity.ok(releaseRestService.get(name,description,status,releaseDate,createdAt,lastUpdateAt));
    }

    @PostMapping
    public ResponseEntity<ReleaseResponse> saveRelease(@RequestBody ReleaseRequest releaseRequest) {
        var releaseResponse = releaseRestService.save(releaseRequest);
        return ResponseEntity.status(releaseResponse.httpStatus()).body(releaseResponse);
    }


    @PutMapping
    public ResponseEntity<ReleaseResponse> updateRelease(@PathVariable("id") String id,
                                                         @RequestBody ReleaseRequest releaseRequest) {
        var updatedRelease = releaseRestService.update(id, releaseRequest);
        return ResponseEntity.status(updatedRelease.httpStatus()).body(updatedRelease);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        releaseRestService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
