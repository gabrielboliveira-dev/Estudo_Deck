package com.example.estudodeck.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ReviewOutcome {
    private final UUID id;
    private final UUID flashcardId;
    private final UUID userId;
    private final boolean success;
    private final CardMaturity maturity;
    private final LocalDateTime timestamp;

    private ReviewOutcome(UUID flashcardId, UUID userId, boolean success, CardMaturity maturity) {
        this.id = UUID.randomUUID();
        this.flashcardId = flashcardId;
        this.userId = userId;
        this.success = success;
        this.maturity = maturity;
        this.timestamp = LocalDateTime.now();
    }

    public static ReviewOutcome create(UUID flashcardId, UUID userId, boolean success, CardMaturity maturity) {
        return new ReviewOutcome(flashcardId, userId, success, maturity);
    }
}
