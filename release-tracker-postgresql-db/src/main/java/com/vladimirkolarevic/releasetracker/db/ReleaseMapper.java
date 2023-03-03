package com.vladimirkolarevic.releasetracker.db;

import com.vladimirkolarevic.releasetracker.db.ReleaseJpaEntity;
import com.vladimirkolarevic.releasetracker.domain.Release;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
interface ReleaseMapper {

    @Mapping(target = "id",source = "uuid")
    Release toDomain(ReleaseJpaEntity releaseJpaEntity);

    @Mapping(target = "uuid",source = "id")
    @Mapping(target = "id",ignore = true)
    ReleaseJpaEntity fromDomain(Release release);
}
