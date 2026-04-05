package com.example.estudodeck.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlashcardTest {

    @Test
    @DisplayName("Deve criar um cartão novo com estado inicial correto do SM-2")
    void shouldCreateNewFlashcard() {
        Flashcard card = Flashcard.createNew(new BasicCardContent("Qual a capital do Brasil?", "Brasília"));
        Assertions.assertNotNull(card.getId());
        assertEquals("Qual a capital do Brasil?", card.getContent().getPrompt());
        assertEquals(0, card.getRepetitions());
        assertEquals(0, card.getIntervalInDays());
        assertEquals(2.5, card.getEasinessFactor());
    }

    @Test
    @DisplayName("Deve calcular a primeira revisão com sucesso (Nota 5)")
    void shouldProcessFirstReviewSuccessfully() {
        Flashcard card = Flashcard.createNew(new BasicCardContent("Pergunta", "Resposta"));
        card.processReview(5);
        assertEquals(1, card.getRepetitions());
        assertEquals(1, card.getIntervalInDays());
        assertEquals(2.6, card.getEasinessFactor(), 0.01);
    }

    @Test
    @DisplayName("Deve resetar o progresso se o usuário esquecer (Nota 0)")
    void shouldResetProgressOnFailure() {
        Flashcard card = Flashcard.createNew(new BasicCardContent("Pergunta", "Resposta"));
        card.processReview(5); // First review, success
        card.processReview(0); // Second review, failure
        assertEquals(0, card.getRepetitions()); // Repetitions reset
        assertEquals(1, card.getIntervalInDays()); // Interval resets to 1
        // EF is not changed on failure in this model, so it should remain 2.6
        assertEquals(2.6, card.getEasinessFactor(), 0.01);
    }
}
