package com.example.estudodeck.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Flashcard {

    private final UUID id;
    private String question;
    private String answer;
    private int repetitions;
    private double easinessFactor;
    private int intervalInDays;
    private LocalDateTime nextDueDate;

    private Flashcard(String question, String answer) {
        this.id = UUID.randomUUID();
        this.question = question;
        this.answer = answer;
        this.repetitions = 0;
        this.easinessFactor = 2.5;
        this.intervalInDays = 0;
        this.nextDueDate = LocalDateTime.now();
    }

    public static Flashcard createNew(String question, String answer) {
        if (question == null || question.isBlank()) throw new IllegalArgumentException("A pergunta não pode ser vazia");
        if (answer == null || answer.isBlank()) throw new IllegalArgumentException("A resposta não pode ser vazia");

        return new Flashcard(question, answer);
    }

    public void processReview(int quality) {
        if (quality < 0 || quality > 5) {
            throw new IllegalArgumentException("A nota deve ser entre 0 e 5");
        }

        if (quality >= 3) {
            calculateSuccesfulInterval();
            updateEasinessFactor(quality);
        } else {
            handleFailure();
        }

        this.nextDueDate = LocalDateTime.now().plusDays(this.intervalInDays);
    }

    private void calculateSuccesfulInterval() {
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
    }

    private void updateEasinessFactor(int quality) {
        double formulaVal = 5 - quality;
        easinessFactor = easinessFactor + (0.1 - (formulaVal * (0.08 + (formulaVal * 0.02))));

        if (easinessFactor < 1.3) {
            easinessFactor = 1.3;
        }
    }
}
