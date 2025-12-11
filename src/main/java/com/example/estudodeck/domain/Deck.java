package com.example.estudodeck.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Deck {

    private final UUID id;
    private String name;
    private final List<Flashcard> cards;

    private Deck(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.cards = new ArrayList<>();
    }

    public static Deck create(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome do baralho é obrigatório.");
        }
        return new Deck(name);
    }

    public void addCard(Flashcard card) {
        if (card == null) {
            throw new IllegalArgumentException("Não é possível adicionar um cartão nulo.");
        }
        this.cards.add(card);
    }

    public List<Flashcard> getPendingCards() {
        LocalDateTime now = LocalDateTime.now();

        return cards.stream()
                .filter(c -> c.getNextDueDate().isBefore(now) || c.getNextDueDate().isEqual(now))
                .toList();
    }

    public List<Flashcard> getCards() {
        return Collections.unmodifiableList(cards);
    }
}