package com.example.estudodeck.infrastructure.persistence.repositories;

import com.example.estudodeck.infrastructure.persistence.entities.ReviewLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SpringDataReviewLogRepository extends JpaRepository<ReviewLogJpaEntity, UUID> {

    @Query("SELECT r FROM ReviewLogJpaEntity r WHERE r.userId = :userId AND r.reviewDate BETWEEN :startDate AND :endDate")
    List<ReviewLogJpaEntity> findByUserIdAndDateRange(@Param("userId") UUID userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT r.reviewDate FROM ReviewLogJpaEntity r WHERE r.userId = :userId")
    List<LocalDate> findDistinctReviewDatesByUserId(@Param("userId") UUID userId);
}
