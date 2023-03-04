package com.vladimirkolarevic.releasetracker.db;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.Type;

@Entity
class ReleaseJpaEntity extends BaseJpaEntity {

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Type(value = PostgresEnumType.class)
    private ReleaseStatusJpaEntity status;

    private LocalDate releaseDate;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdateAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReleaseStatusJpaEntity getStatus() {
        return status;
    }

    public void setStatus(ReleaseStatusJpaEntity status) {
        this.status = status;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(LocalDateTime lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }
}
