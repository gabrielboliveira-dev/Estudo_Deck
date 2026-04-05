package com.example.estudodeck.infrastructure.persistence.entities;

import com.example.estudodeck.domain.CardMaturity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "review_outcomes")
@Getter
@Setter
public class ReviewOutcomeJpaEntity {

    @Id
    private UUID id;
    private UUID flashcardId;

    @Column(nullable = false)
    private UUID userId;

    private boolean success;

    @Enumerated(EnumType.STRING)
    private CardMaturity maturity;

    private LocalDateTime timestamp;
}
