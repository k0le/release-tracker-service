package com.vladimirkolarevic.releasetracker.rest;

public record ReleaseRequest(
    String name,
    String description,
    ReleaseResponseStatus status,
    String releaseDate,
    String createdAt,
    String lastUpdateAt
) {
}
