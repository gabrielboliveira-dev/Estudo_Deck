package com.example.estudodeck.infrastructure.persistence.repositories;

import com.example.estudodeck.application.dtos.ReviewForecastDto;
import com.example.estudodeck.infrastructure.persistence.entities.FlashcardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SpringDataFlashcardRepository extends JpaRepository<FlashcardJpaEntity, UUID> {

    @Query("""
        SELECT new com.example.estudodeck.application.dtos.ReviewForecastDto(CAST(f.nextDueDate AS LocalDate), COUNT(f.id))
        FROM FlashcardJpaEntity f
        JOIN f.deck d
        WHERE d.userId = :userId AND f.nextDueDate BETWEEN :startDate AND :endDate
        GROUP BY CAST(f.nextDueDate AS LocalDate)
        ORDER BY CAST(f.nextDueDate AS LocalDate) ASC
    """)
    List<ReviewForecastDto> getReviewForecast(@Param("userId") UUID userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
