package com.vladimirkolarevic.releasetracker.db;

import com.vladimirkolarevic.releasetracker.domain.ReleaseStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CriteriaRepositoryImpl implements CriteriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ReleaseJpaEntity> filter(String name, String description, ReleaseStatus status, LocalDate releaseDate,
                                         LocalDateTime createdAt, LocalDateTime lastUpdateAt) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(ReleaseJpaEntity.class);
        var rootRelease = criteriaQuery.from(ReleaseJpaEntity.class);
        Predicate whereClause = null;
        if (name != null) {
            whereClause = criteriaBuilder.and(criteriaBuilder.equal(rootRelease.get("name"), name));
        }
        if(description!=null){
            whereClause = criteriaBuilder.and(criteriaBuilder.equal(rootRelease.get("description"), description));
        }
        if(status!=null){
            whereClause = criteriaBuilder.and(criteriaBuilder.equal(rootRelease.get("status"), status.name()));
        }
        if(releaseDate!=null){
            whereClause = criteriaBuilder.and(criteriaBuilder.equal(rootRelease.get("status"), releaseDate));
        }
        if(createdAt!=null){
            whereClause = criteriaBuilder.and(criteriaBuilder.equal(rootRelease.get("createdAt"), createdAt));
        }
        if(lastUpdateAt!=null){
            whereClause = criteriaBuilder.and(criteriaBuilder.equal(rootRelease.get("lastUpdateAt"), lastUpdateAt));
        }
        if(whereClause != null) {
            criteriaQuery.where(whereClause);
        }
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
