package com.example.estudodeck.domain;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class ReviewLog {
    private final UUID id;
    private final LocalDate reviewDate;
    private final UUID userId;

    private ReviewLog(LocalDate reviewDate, UUID userId) {
        this.id = UUID.randomUUID();
        this.reviewDate = reviewDate;
        this.userId = userId;
    }

    public static ReviewLog create(UUID userId) {
        return new ReviewLog(LocalDate.now(), userId);
    }

    public static ReviewLog restore(UUID id, LocalDate reviewDate, UUID userId) {
        return new ReviewLog(id, reviewDate, userId);
    }

    private ReviewLog(UUID id, LocalDate reviewDate, UUID userId) {
        this.id = id;
        this.reviewDate = reviewDate;
        this.userId = userId;
    }
}
