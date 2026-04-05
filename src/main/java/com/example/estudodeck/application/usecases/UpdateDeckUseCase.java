package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateDeckUseCase {

    private final DeckRepository deckRepository;
    private final UserContext userContext;

    public UpdateDeckUseCase(DeckRepository deckRepository, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.userContext = userContext;
    }

    public void execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(input.deckId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        deck.changeName(input.newName());

        deckRepository.save(deck);
    }

    public record Input(UUID deckId, String newName) {}
}
