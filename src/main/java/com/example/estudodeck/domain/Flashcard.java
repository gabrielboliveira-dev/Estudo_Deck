package com.example.estudodeck.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Flashcard {

    private static final int LEECH_THRESHOLD = 8;
    private static final int MATURITY_THRESHOLD_DAYS = 21;

    private final UUID id;
    private CardContent content;
    private int repetitions;
    private double easinessFactor;
    private int intervalInDays;
    private LocalDateTime nextDueDate;
    private int leechCount;
    private boolean suspended;

    private Flashcard(CardContent content) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.repetitions = 0;
        this.easinessFactor = 2.5;
        this.intervalInDays = 0;
        this.nextDueDate = LocalDateTime.now();
        this.leechCount = 0;
        this.suspended = false;
    }

    public static Flashcard createNew(CardContent content) {
        if (content == null) {
            throw new IllegalArgumentException("Card content cannot be null");
        }
        return new Flashcard(content);
    }

    public void processReview(int quality) {
        if (quality < 0 || quality > 5) {
            throw new IllegalArgumentException("Quality must be between 0 and 5");
        }

        if (quality >= 3) {
            // Success
            this.leechCount = 0; // Reset leech counter on success
            calculateSuccessfulInterval();
            updateEasinessFactor(quality);
        } else {
            // Failure
            handleFailure();
        }

        this.nextDueDate = LocalDateTime.now().plusDays(this.intervalInDays);
    }

    public CardMaturity getMaturity() {
        return this.intervalInDays >= MATURITY_THRESHOLD_DAYS ? CardMaturity.MATURE : CardMaturity.YOUNG;
    }

    private void calculateSuccessfulInterval() {
        if (repetitions == 0) {
            intervalInDays = 1;
        } else if (repetitions == 1) {
            intervalInDays = 6;
        } else {
            intervalInDays = (int) Math.ceil(intervalInDays * easinessFactor);
        }
        repetitions++;
    }

    private void handleFailure() {
        repetitions = 0;
        intervalInDays = 1;
        this.leechCount++;
        if (this.leechCount >= LEECH_THRESHOLD) {
            this.suspended = true;
        }
    }

    public void unsuspend() {
        this.suspended = false;
        this.leechCount = 0; // Reset counter when manually unsuspended
    }

    private void updateEasinessFactor(int quality) {
        double formulaVal = 5 - quality;
        easinessFactor = easinessFactor + (0.1 - (formulaVal * (0.08 + (formulaVal * 0.02))));

        if (easinessFactor < 1.3) {
            easinessFactor = 1.3;
        }
    }

    public static Flashcard restore(UUID id, CardContent content, int repetitions, double easinessFactor, int intervalInDays, LocalDateTime nextDueDate, int leechCount, boolean suspended) {
        return new Flashcard(id, content, repetitions, easinessFactor, intervalInDays, nextDueDate, leechCount, suspended);
    }

    private Flashcard(UUID id, CardContent content, int repetitions, double easinessFactor, int intervalInDays, LocalDateTime nextDueDate, int leechCount, boolean suspended) {
        this.id = id;
        this.content = content;
        this.repetitions = repetitions;
        this.easinessFactor = easinessFactor;
        this.intervalInDays = intervalInDays;
        this.nextDueDate = nextDueDate;
        this.leechCount = leechCount;
        this.suspended = suspended;
    }
}
