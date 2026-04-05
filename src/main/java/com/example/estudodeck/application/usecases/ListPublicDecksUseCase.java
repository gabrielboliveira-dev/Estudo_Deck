package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListPublicDecksUseCase {

    private final DeckRepository deckRepository;

    public ListPublicDecksUseCase(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public List<Deck> execute() {
        // This requires a new method in the repository
        return deckRepository.findAllPublic();
    }
}
