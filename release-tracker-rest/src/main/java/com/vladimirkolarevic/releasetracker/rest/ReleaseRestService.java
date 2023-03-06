package com.vladimirkolarevic.releasetracker.rest;

import com.vladimirkolarevic.releasetracker.domain.Release;
import com.vladimirkolarevic.releasetracker.domain.ReleaseService;
import com.vladimirkolarevic.releasetracker.domain.ReleaseStatus;
import com.vladimirkolarevic.releasetracker.domain.exception.NonExistentReleaseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
class ReleaseRestService {

    private final ReleaseService releaseService;
    private final ReleaseRestMapper releaseRestMapper = Mappers.getMapper(ReleaseRestMapper.class);

    public ReleaseRestService(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }


    public ReleaseResponse get(String id) {
        var uuid = UUID.fromString(id);
        return releaseRestMapper.toView(releaseService.get(uuid), HttpStatus.OK);
    }

    public List<ReleaseResponse> get(String name, String description, ReleaseResponseStatus status, String releaseDate,
                                     String createdAt, String lastUpdateAt) {
        var releaseStatus = status != null ? ReleaseStatus.valueOf(status.name()) : null;
        var searchReleaseDate =
            releaseDate != null ? LocalDate.from(DateTimeFormatter.ISO_DATE.parse(releaseDate)) : null;
        var searchCreatedAt =
            createdAt != null ? LocalDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(createdAt)) : null;
        var searchLastUpdateAt =
            lastUpdateAt != null ? LocalDateTime.from(DateTimeFormatter.ISO_DATE.parse(lastUpdateAt)) : null;

        return releaseService.list(name, description, releaseStatus, searchReleaseDate, searchCreatedAt,
            searchLastUpdateAt).stream().map(releaseRestMapper::toView).toList();
    }

    public ReleaseResponse save(ReleaseRequest releaseRequest) {
        var release = releaseRestMapper.toDomain(releaseRequest, null);
        var savedRelease = releaseService.save(release);
        return releaseRestMapper.toView(savedRelease, HttpStatus.CREATED);
    }

    public ReleaseResponse update(String id, ReleaseRequest releaseRequest) {
        var uuid = UUID.fromString(id);
        var httpStatus = HttpStatus.OK;
        var release = releaseRestMapper.toDomain(releaseRequest, uuid);
        Release updatedRelease = null;
        try {
            updatedRelease = releaseService.update(release);
        } catch (NonExistentReleaseException e) {
            httpStatus = HttpStatus.CREATED;
            updatedRelease = releaseService.save(release);
        }
        return releaseRestMapper.toView(updatedRelease, httpStatus);
    }

    public void delete(String id) {
        var uuid = UUID.fromString(id);
        releaseService.delete(uuid);
    }


}
