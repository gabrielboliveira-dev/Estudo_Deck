package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;

public class CreateDeckUseCase {

    private final DeckRepository repository;

    public CreateDeckUseCase(DeckRepository repository) {
        this.repository = repository;
    }

    public Deck execute(String name) {
        Deck newDeck = Deck.create(name);

        return repository.save(newDeck);
    }
}