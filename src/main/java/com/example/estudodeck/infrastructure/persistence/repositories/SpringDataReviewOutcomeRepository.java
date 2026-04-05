package com.example.estudodeck.infrastructure.persistence.repositories;

import com.example.estudodeck.infrastructure.persistence.entities.ReviewOutcomeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataReviewOutcomeRepository extends JpaRepository<ReviewOutcomeJpaEntity, UUID> {
    List<ReviewOutcomeJpaEntity> findByUserId(UUID userId);
}
