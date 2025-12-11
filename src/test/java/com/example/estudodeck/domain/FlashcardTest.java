package com.example.estudodeck.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FlashcardTest {

    @Test
    @DisplayName("Deve criar um cartão novo com estado inicial correto do SM-2")
    void shouldCreateNewFlashcard() {
        Flashcard card = Flashcard.createNew("Qual a capital do Brasil?", "Brasília");
        Assertions.assertNotNull(card.getId());
        Assertions.assertEquals("Qual a capital do Brasil?", card.getQuestion());
        Assertions.assertEquals(0, card.getRepetitions());
        Assertions.assertEquals(0, card.getIntervalInDays());
        Assertions.assertEquals(2.5, card.getEasinessFactor());
    }

    @Test
    @DisplayName("Deve calcular a primeira revisão com sucesso (Nota 5)")
    void shouldProcessFirstReviewSuccessfully() {
        Flashcard card = Flashcard.createNew("Pergunta", "Resposta");
        card.processReview(5);
        Assertions.assertEquals(1, card.getRepetitions());
        Assertions.assertEquals(1, card.getIntervalInDays());
        Assertions.assertEquals(2.6, card.getEasinessFactor(), 0.01);
    }

    @Test
    @DisplayName("Deve resetar o progresso se o usuário esquecer (Nota 0)")
    void shouldResetProgressOnFailure() {
        Flashcard card = Flashcard.createNew("Pergunta", "Resposta");
        card.processReview(5);
        card.processReview(0);
        Assertions.assertEquals(0, card.getRepetitions());
        Assertions.assertEquals(1, card.getIntervalInDays());
        Assertions.assertEquals(2.6, card.getEasinessFactor(), 0.01);
    }
}