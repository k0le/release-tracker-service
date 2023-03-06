package com.vladimirkolarevic.releasetracker.rest;

import com.vladimirkolarevic.releasetracker.domain.Release;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;

@Mapper
interface ReleaseRestMapper {


    @Mapping(target = "status", source = "release.status")
    ReleaseResponse toView(Release release, HttpStatus httpStatus);

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toFormattedDateTime")
    @Mapping(target = "lastUpdateAt", source = "lastUpdateAt", qualifiedByName = "toFormattedDateTime")
    @Mapping(target = "releaseDate", source = "releaseDate", qualifiedByName = "toFormattedDate")
    ReleaseResponse toView(Release release);

    @Mapping(target = "status", source = "releaseRequest.status")
    Release toDomain(ReleaseRequest releaseRequest, UUID id);

    @Named("toFormattedDateTime")
    default String toFormattedDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ISO_DATE_TIME) : null;
    }

    @Named("toFormattedDate")
    default String toFormattedDate(LocalDate localDate) {
        return localDate != null ? localDate.format(DateTimeFormatter.ISO_DATE) : null;
    }
}
